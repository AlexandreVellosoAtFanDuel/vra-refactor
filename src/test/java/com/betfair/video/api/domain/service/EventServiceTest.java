package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.dto.ContentTypeDto;
import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.TypeChannel;
import com.betfair.video.api.domain.entity.TypeMobileDevice;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import com.betfair.video.api.domain.valueobject.UserPermissions;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventService Tests")
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private ExternalIdMapper externalIdMapper;

    @Mock
    private TypeStreamMapper typeStreamMapper;

    @Mock
    private StreamService streamService;

    @BeforeEach
    void setUp() {
        // 3 is Fanduel
        ReflectionTestUtils.setField(eventService, "streamingBrandId", 3);
    }

    @Test
    @DisplayName("Should retrieve schedule by external ID")
    void shouldRetrieveScheduleByExternalId() {
        // Given
        User user = mock(User.class);
        UserPermissions userPermissions = new UserPermissions(Set.of(ServicePermission.VIDEO.name()), null, null, null, null);
        when(user.permissions()).thenReturn(userPermissions);

        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");
        when(context.user()).thenReturn(user);

        ExternalId externalId = new ExternalId(ExternalIdSource.BETFAIR_EVENT, Map.of("12345", Collections.emptyList()));
        when(externalIdMapper.map(eq(context), eq(ExternalIdSource.BETFAIR_EVENT), any()))
                .thenReturn(externalId);

        when(typeStreamMapper.convertContentTypeToStreamTypeId(ContentTypeDto.VID))
                .thenReturn(TypeStream.VID.getId());

        VideoStreamInfo videoStreamInfoMock = mock(VideoStreamInfo.class);
        when(streamService.getStreamInfoByExternalId(any(VideoStreamInfoByExternalIdSearchKey.class), eq(context), anyBoolean()))
                .thenReturn(videoStreamInfoMock);

        // When
        VideoStreamInfo result = eventService.retrieveScheduleByExternalId(
                context, "1", "12345", null, null, null, null, null, null, null, null, ContentTypeDto.VID, null, null);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should fail when user doesn't have permission")
    void shouldFailWhenUserDoesNotHavePermission() {
        // Given
        User user = mock(User.class);
        UserPermissions userPermissions = new UserPermissions(Collections.emptySet(), null, null, null, null);
        when(user.permissions()).thenReturn(userPermissions);

        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");
        when(context.user()).thenReturn(user);

        // When & Then
        assertThatThrownBy(() -> eventService.retrieveScheduleByExternalId(context, null, null, null, null, null, null, null, null, null, null, null, null, null))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.Forbidden);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail when channel type id is null")
    void shouldFailWhenChannelTypeIdIsNull() {
        // Given
        User user = mock(User.class);
        UserPermissions userPermissions = new UserPermissions(Set.of(ServicePermission.VIDEO.name()), null, null, null, null);
        when(user.permissions()).thenReturn(userPermissions);

        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");
        when(context.user()).thenReturn(user);

        ExternalId externalId = new ExternalId(ExternalIdSource.BETFAIR_EVENT, Map.of("12345", Collections.emptyList()));
        when(externalIdMapper.map(eq(context), eq(ExternalIdSource.BETFAIR_EVENT), any()))
                .thenReturn(externalId);

        when(typeStreamMapper.convertContentTypeToStreamTypeId(ContentTypeDto.VID))
                .thenReturn(TypeStream.VID.getId());

        // When & Then
        assertThatThrownBy(() -> eventService.retrieveScheduleByExternalId(
                context, "1", "12345", TypeChannel.NULL.getId(), null, null, null, null, null, null, null, ContentTypeDto.VID, null, null))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail when channel type is web and mobile device id is not null")
    void shouldFailWhenChannelTypeIsWebAndMobileDeviceIdIsNotNull() {
        // Given
        User user = mock(User.class);
        UserPermissions userPermissions = new UserPermissions(Set.of(ServicePermission.VIDEO.name()), null, null, null, null);
        when(user.permissions()).thenReturn(userPermissions);

        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");
        when(context.user()).thenReturn(user);

        ExternalId externalId = new ExternalId(ExternalIdSource.BETFAIR_EVENT, Map.of("12345", Collections.emptyList()));
        when(externalIdMapper.map(eq(context), eq(ExternalIdSource.BETFAIR_EVENT), any()))
                .thenReturn(externalId);

        when(typeStreamMapper.convertContentTypeToStreamTypeId(ContentTypeDto.VID))
                .thenReturn(TypeStream.VID.getId());

        // When & Then
        assertThatThrownBy(() -> eventService.retrieveScheduleByExternalId(
                context, "1", "12345", TypeChannel.WEB.getId(), null, TypeMobileDevice.ANDROID_PHONE.getId(), null, null, null, null, null, ContentTypeDto.VID, null, null))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.BadRequest);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INVALID_INPUT);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should fail when channel type is mobile and it's not a valid mobile device")
    void shouldFailWhenChannelTypeIsMobileAndNotValidMobileDevice() {
        // Given
        User user = mock(User.class);
        UserPermissions userPermissions = new UserPermissions(Set.of(ServicePermission.VIDEO.name()), null, null, null, null);
        when(user.permissions()).thenReturn(userPermissions);

        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");
        when(context.user()).thenReturn(user);

        ExternalId externalId = new ExternalId(ExternalIdSource.BETFAIR_EVENT, Map.of("12345", Collections.emptyList()));
        when(externalIdMapper.map(eq(context), eq(ExternalIdSource.BETFAIR_EVENT), any()))
                .thenReturn(externalId);

        when(typeStreamMapper.convertContentTypeToStreamTypeId(ContentTypeDto.VID))
                .thenReturn(TypeStream.VID.getId());

        // When & Then
        assertThatThrownBy(() -> eventService.retrieveScheduleByExternalId(
                context, "1", "12345", TypeChannel.MOBILE.getId(), null, TypeMobileDevice.NULL.getId(), null, null, null, null, null, ContentTypeDto.VID, null, null))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.BadRequest);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INVALID_INPUT);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
