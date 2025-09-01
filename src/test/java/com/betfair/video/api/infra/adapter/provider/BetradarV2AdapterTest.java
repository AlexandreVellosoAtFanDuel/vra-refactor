package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.StreamingFormat;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.infra.client.BetRadarV2Client;
import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.infra.dto.betradarv2.BaseDto;
import com.betfair.video.api.infra.dto.betradarv2.ContentDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamUrlDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BetradarV2ProviderAdapter Tests")
class BetradarV2AdapterTest {

    @InjectMocks
    BetradarV2Adapter betradarV2Adapter;

    @Mock
    BetRadarV2Client betRadarV2Client;

    @Mock
    StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    @Mock
    ConfigurationItemsPort configurationItemsRepository;

    @Test
    @DisplayName("Test provider is enabled")
    void testProviderIsEnabled() {
        // Given
        ReflectionTestUtils.setField(betradarV2Adapter, "isEnabled", "true");

        // When
        boolean isEnabled = betradarV2Adapter.isEnabled();

        // Then
        assertThat(isEnabled).isTrue();
    }

    @Test
    @DisplayName("Test provider is not enabled")
    void testProviderIsNotEnabled() {
        // Given
        ReflectionTestUtils.setField(betradarV2Adapter, "isEnabled", "false");

        // When
        boolean isEnabled = betradarV2Adapter.isEnabled();

        // Then
        assertThat(isEnabled).isFalse();
    }

    @Test
    @DisplayName("Test available video quality values")
    void testAvailableVideoQualityValues() {
        // When
        Set<VideoQuality> qualities = betradarV2Adapter.getAvailableVideoQualityValues();

        // Then
        assertThat(qualities).isNotEmpty();
        assertThat(qualities).hasSize(1);
        assertThat(qualities).contains(VideoQuality.HIGH);
    }

    @Test
    @DisplayName("Test stream details - happy path")
    void testStreamDetailsHappyPath() {
        // Given
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamStatusIds", "2");
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamProductIds", "2");

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.providerEventId()).thenReturn("event123");

        User user = mock(User.class);
        when(user.ip()).thenReturn("User IP");

        RequestContext context = mock(RequestContext.class);
        when(context.user()).thenReturn(user);

        StreamParams streamParams = mock(StreamParams.class);

        StreamDto streamDto = mock(StreamDto.class);
        when(streamDto.id()).thenReturn("stream123");
        when(streamDto.streamStatus()).thenReturn(new BaseDto("av:event_status:2", "Confirmed"));
        when(streamDto.product()).thenReturn(new BaseDto("av:product:2", "Live Channel Online"));

        ContentDto contentDto = mock(ContentDto.class);
        when(contentDto.isMain()).thenReturn(true);
        when(contentDto.streams()).thenReturn(Collections.singletonList(streamDto));

        AudioVisualEventDto eventDto = mock(AudioVisualEventDto.class);
        when(eventDto.id()).thenReturn("av:event:event123");
        when(eventDto.contents()).thenReturn(Collections.singletonList(contentDto));

        when(betRadarV2Client.getAudioVisualEvents(anyString(), anyString())).thenReturn(Collections.singletonList(eventDto));

        when(configurationItemsRepository.findPreferredStreamingFormat(eq(Provider.BETRADAR_V2), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(StreamingFormat.HLS);

        StreamUrlDto streamUrlDto = new StreamUrlDto("Stream URL", "Stream name");
        when(betRadarV2Client.getStreamLink(anyString(), anyString(), anyString()))
                .thenReturn(streamUrlDto);

        // When
        StreamDetails streamDetails = betradarV2Adapter.getStreamDetails(scheduleItem, context, streamParams);

        // Then
        assertThat(streamDetails).isNotNull();
        assertThat(streamDetails.endpoint()).isEqualTo(streamUrlDto.url());
        assertThat(streamDetails.quality()).isEqualTo(VideoQuality.HIGH);

        var params = streamDetails.params();
        assertThat(params).isNotNull();
        assertThat(params.size()).isEqualTo(1);
        assertThat(params.get("streamFormat")).isEqualTo(StreamingFormat.HLS.getValue());
    }

    @Test
    @DisplayName("Test stream details - event not found")
    void testStreamDetailsEventNotFound() {
        // Given
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamStatusIds", "2");
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamProductIds", "2");

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.providerEventId()).thenReturn("notExist123");

        RequestContext context = mock(RequestContext.class);
        User user = mock(User.class);
        StreamParams streamParams = mock(StreamParams.class);

        AudioVisualEventDto eventDto = mock(AudioVisualEventDto.class);
        when(eventDto.id()).thenReturn("av:event:event123");

        when(betRadarV2Client.getAudioVisualEvents(anyString(), anyString())).thenReturn(Collections.singletonList(eventDto));

        // When & Then
        assertThatThrownBy(() -> betradarV2Adapter.getStreamDetails(scheduleItem, context, streamParams))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Test stream details - invalid stream status")
    void testStreamDetailsInvalidStreamStatus() {
        // Given
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamStatusIds", "2");
        ReflectionTestUtils.setField(betradarV2Adapter, "recommendedStreamProductIds", "2");

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.providerEventId()).thenReturn("event123");

        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);
        when(context.user()).thenReturn(user);

        StreamParams streamParams = mock(StreamParams.class);

        StreamDto streamDto = mock(StreamDto.class);
        when(streamDto.streamStatus()).thenReturn(new BaseDto("av:event_status:1", "Inactive")); // Fake status

        ContentDto contentDto = mock(ContentDto.class);
        when(contentDto.isMain()).thenReturn(true);
        when(contentDto.streams()).thenReturn(Collections.singletonList(streamDto));

        AudioVisualEventDto eventDto = mock(AudioVisualEventDto.class);
        when(eventDto.id()).thenReturn("av:event:event123");
        when(eventDto.contents()).thenReturn(Collections.singletonList(contentDto));

        when(betRadarV2Client.getAudioVisualEvents(anyString(), anyString())).thenReturn(Collections.singletonList(eventDto));

        // When & Then
        assertThatThrownBy(() -> betradarV2Adapter.getStreamDetails(scheduleItem, context, streamParams))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
