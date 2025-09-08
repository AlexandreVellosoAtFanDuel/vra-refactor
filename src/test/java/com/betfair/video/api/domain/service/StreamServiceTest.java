package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.ProviderEventKey;
import com.betfair.video.api.domain.dto.entity.ReferenceType;
import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.ScheduleItemMappingKey;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.dto.valueobject.StreamDetails;
import com.betfair.video.api.domain.dto.valueobject.StreamParams;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamState;
import com.betfair.video.api.domain.dto.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoSearchKeyWrapper;
import com.betfair.video.api.domain.dto.valueobject.ScheduleItemMapper;
import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.output.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.output.ProviderFactoryPort;
import com.betfair.video.api.domain.port.output.ReferenceTypePort;
import com.betfair.video.api.domain.port.output.StreamingProviderPort;
import com.betfair.video.api.infra.input.rest.exception.ResponseCode;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIException;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIExceptionErrorCodeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

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

    @Mock
    private ReferenceTypePort referenceTypePort;

    @Test
    @DisplayName("Should retrieve stream by external ID")
    void shouldRetrieveStreamByExternalId() {
        // Given
        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);
        when(context.user()).thenReturn(user);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);

        ScheduleItemMapper mapper = mock(ScheduleItemMapper.class);
        when(mapper.scheduleItemMappingKey()).thenReturn(new ScheduleItemMappingKey("videoItemId", new ProviderEventKey(1, "12345", "EVENT:12345")));

        Set<ScheduleItemMapper> mappings = Set.of(mapper);
        when(scheduleItem.mappings()).thenReturn(mappings);

        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context)))
                .thenReturn(scheduleItem);

        when(scheduleItemService.getVideoStreamStateBasedOnScheduleItem(any(ScheduleItem.class)))
                .thenReturn(VideoStreamState.STREAM);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(true);

        StreamDetails streamDetails = mock(StreamDetails.class);
        when(streamingProvider.getStreamDetails(eq(context), any(ScheduleItem.class), any(StreamParams.class))).thenReturn(streamDetails);

        when(directStreamConfigPort.isProviderInList(anyInt(), anyInt()))
                .thenReturn(true);

        when(inlineStreamConfigPort.isProviderInList(anyInt(), anyInt()))
                .thenReturn(true);

        when(configurationItemsPort.getDefaultVideoQuality(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn("HIGH");

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt()))
                .thenReturn(streamingProvider);

        when(permissionService.checkUserPermissionsAgainstItem(any(ScheduleItem.class), eq(user)))
                .thenReturn(true);

        when(betsCheckService.getBBVStatus(any(VideoRequestIdentifier.class), any(ScheduleItem.class), eq(context)))
                .thenReturn(BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG);

        when(videoStreamInfoMapper.map(any(ScheduleItem.class), any(StreamDetails.class), any(), any(), anyBoolean(), anyBoolean(), any(), any(), any(), any(), any(), anyBoolean(), any(), any(), any(), any(), any()))
                .thenReturn(mock(VideoStreamInfo.class));

        when(referenceTypePort.findReferenceTypeById(any(), any()))
                .thenReturn(mock(ReferenceType.class));

        // When
        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        VideoStreamInfo videoStreamInfo = streamService.getStreamInfoByExternalId(searchKey, context, false);

        // Then
        assertThat(videoStreamInfo).isNotNull();
    }

    @Test
    @DisplayName("Should fail if no provider exist for video type")
    void shouldFailIfNoProviderExistsForVideoType() {
        // Given
        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);
        when(context.user()).thenReturn(user);

        Geolocation geolocation = mock(Geolocation.class);
        when(user.geolocation()).thenReturn(geolocation);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        when(referenceTypePort.findReferenceTypeById(any(), any()))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, false))
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

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .build();

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.providerId()).thenReturn(1);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context)))
                .thenReturn(scheduleItem);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(1))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, false))
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

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context)))
                .thenReturn(scheduleItem);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(false);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt()))
                .thenReturn(streamingProvider);

        when(referenceTypePort.findReferenceTypeById(any(), any()))
                .thenReturn(mock(ReferenceType.class));

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, false))
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
        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);
        when(context.user()).thenReturn(user);

        Geolocation geolocation = mock(Geolocation.class);
        when(user.geolocation()).thenReturn(geolocation);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(ExternalIdSource.BETFAIR_EVENT)
                .primaryId("12345")
                .providerId(1)
                .build();

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.providerId()).thenReturn(1);
        when(scheduleItemService.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(context)))
                .thenReturn(scheduleItem);

        StreamingProviderPort streamingProvider = mock(StreamingProviderPort.class);
        when(streamingProvider.isEnabled()).thenReturn(true);

        when(providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(anyInt()))
                .thenReturn(streamingProvider);

        when(permissionService.checkUserPermissionsAgainstItem(any(ScheduleItem.class), eq(user)))
                .thenReturn(false);

        when(referenceTypePort.findReferenceTypeById(any(), any()))
                .thenReturn(mock(ReferenceType.class));

        // When & Then
        assertThatThrownBy(() -> streamService.getStreamInfoByExternalId(searchKey, context, false))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.Forbidden);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
