package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.port.ScheduleItemPort;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.Geolocation;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
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
    private ScheduleItemPort scheduleItemPort;

    @Mock
    private ConfigurationItemsPort configurationItemsPort;

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
        when(scheduleItemPort.getScheduleItemByStreamKey(any(VideoStreamInfoSearchKeyWrapper.class), eq(user)))
                .thenReturn(scheduleItem);

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
    @DisplayName("Should fail if no provider was found")
    void shouldFailIfNoProviderFound() {
        // Given
        RequestContext context = mock(RequestContext.class);
        when(context.uuid()).thenReturn("test-uuid");

        User user = mock(User.class);
        Geolocation geolocation = mock(Geolocation.class);
        when(user.geolocation()).thenReturn(geolocation);
        when(geolocation.countryCode()).thenReturn("US");
        when(geolocation.subDivisionCode()).thenReturn("CA");

        when(referenceTypesPort.findReferenceTypeById(eq(1), eq(ReferenceTypeId.VIDEO_PROVIDER)))
                .thenReturn(null);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
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

}
