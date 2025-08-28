package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.Geolocation;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StreamService Tests")
class StreamServiceTest {

    @InjectMocks
    private StreamService streamService;

    @Mock
    private ReferenceTypesPort referenceTypesPort;

    @Mock
    private ConfigurationItemsPort configurationItemsPort;

    @Mock
    private ProviderFactoryPort providerFactoryPort;

    @Mock
    private PermissionService permissionService;

    @Mock
    private ScheduleItemService scheduleItemService;

    @Mock
    private BetsCheckService betsCheckService;

    @Mock
    private DirectStreamConfigPort directStreamConfigPort;

    @Mock
    private InlineStreamConfigPort inlineStreamConfigPort;

    @Mock
    private VideoStreamInfoMapper videoStreamInfoMapper;

    @Test
    @DisplayName("Should retrieve stream by external ID")
    void shouldRetrieveStreamByExternalId() {
        // Given
        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);

        ReferenceType referenceType = mock(ReferenceType.class);
        when(referenceTypesPort.findReferenceTypeById(anyInt(), eq(ReferenceTypeId.VIDEO_PROVIDER)))
                .thenReturn(referenceType);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context), eq(user)))
                .thenReturn(scheduleItem);

        when(scheduleItemService.getVideoStreamStateBasedOnScheduleItem(any(ScheduleItem.class)))
                .thenReturn(VideoStreamState.STREAM);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(true);

        StreamDetails streamDetails = mock(StreamDetails.class);
        when(streamingProvider.getStreamDetails(any(ScheduleItem.class), eq(user), any(StreamParams.class))).thenReturn(streamDetails);

        when(directStreamConfigPort.isProviderInList(anyInt(), anyInt()))
                .thenReturn(true);

        when(inlineStreamConfigPort.isProviderInList(anyInt(), anyInt()))
                .thenReturn(true);

        when(configurationItemsPort.getDefaultVideoQuality(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn("HIGH");

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt(), anyInt()))
                .thenReturn(streamingProvider);

        when(permissionService.checkUserPermissionsAgainstItem(any(ScheduleItem.class), eq(user)))
                .thenReturn(true);

        when(betsCheckService.getBBVStatus(any(VideoRequestIdentifier.class), any(ScheduleItem.class), eq(user), anyBoolean()))
                .thenReturn(BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG);

        when(videoStreamInfoMapper.map(any(ScheduleItem.class), any(StreamDetails.class), any(), any(), anyBoolean(), anyBoolean(), any(), any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any()))
                .thenReturn(mock(VideoStreamInfo.class));

        // When
        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        VideoStreamInfo videoStreamInfo = streamService.getStreamInfoByExternalId(searchKey, context, user, false);

        // Then
        assertThat(videoStreamInfo).isNotNull();
    }

    @Test
    @DisplayName("Should fail if no provider exist for video type")
    void shouldFailIfNoProviderExistsForVideoType() {
        // Given
        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);

        Geolocation geolocation = mock(Geolocation.class);
        when(user.geolocation()).thenReturn(geolocation);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, user, false))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.BadRequest);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INVALID_INPUT);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail if no provider was found")
    void shouldFailIfNoProviderFound() {
        // Given
        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        ReferenceType referenceType = mock(ReferenceType.class);
        when(referenceTypesPort.findReferenceTypeById(anyInt(), eq(ReferenceTypeId.VIDEO_PROVIDER)))
                .thenReturn(referenceType);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context), eq(user)))
                .thenReturn(scheduleItem);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt(), anyInt()))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, user, false))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.BadRequest);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INVALID_INPUT);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail if provider is not enabled")
    void shouldFailIfProviderNotEnabled() {
        // Given
        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        ReferenceType referenceType = mock(ReferenceType.class);
        when(referenceTypesPort.findReferenceTypeById(anyInt(), eq(ReferenceTypeId.VIDEO_PROVIDER)))
                .thenReturn(referenceType);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context), eq(user)))
                .thenReturn(scheduleItem);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(false);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt(), anyInt()))
                .thenReturn(streamingProvider);

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, user, false))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail if user does not have permissions")
    void shouldFailIfUserDoesNotHavePermissions() {
        // Given
        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);

        Geolocation geolocation = mock(Geolocation.class);
        when(user.geolocation()).thenReturn(geolocation);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        ReferenceType referenceType = mock(ReferenceType.class);
        when(referenceTypesPort.findReferenceTypeById(anyInt(), eq(ReferenceTypeId.VIDEO_PROVIDER)))
                .thenReturn(referenceType);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context), eq(user)))
                .thenReturn(scheduleItem);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(true);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt(), anyInt()))
                .thenReturn(streamingProvider);

        when(permissionService.checkUserPermissionsAgainstItem(any(ScheduleItem.class), eq(user)))
                .thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, user, false))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.Forbidden);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
