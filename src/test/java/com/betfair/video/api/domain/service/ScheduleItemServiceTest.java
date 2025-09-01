package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.ScheduleItemData;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.VideoStreamInfoPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleItemService Tests")
class ScheduleItemServiceTest {

    @Mock
    StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    @Mock
    private VideoStreamInfoPort videoStreamInfoPort;

    @Mock
    private PermissionService permissionService;

    @Mock
    private ConfigurationItemsPort configurationItemsPort;

    private MockedStatic<DateUtils> dateUtilsMock;

    @InjectMocks
    private ScheduleItemService scheduleItemService;

    @BeforeEach
    void setUp() {
        dateUtilsMock = mockStatic(DateUtils.class);
    }

    @AfterEach
    void tearDown() {
        dateUtilsMock.close();
    }

    @Test
    @DisplayName("Should get video stream state based on schedule item - no leading or trail time")
    void shouldGetVideoStreamStateBasedOnScheduleItemNoLeadingOrTrailTime() {
        // Given
        ScheduleItemData actualProviderData = mock(ScheduleItemData.class);

        Date now = new Date();

        // Create a Date from 30 minutes before
        Date startDate = new Date(now.getTime() - 30 * 60 * 1000); // 30 minutes before
        when(actualProviderData.getStart()).thenReturn(startDate);

        Date endDate = new Date();
        endDate.setTime(now.getTime() + 60 * 60 * 1000); // 1 hour later
        when(actualProviderData.getEnd()).thenReturn(endDate);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.leadTime()).thenReturn(null);
        when(scheduleItem.trailTime()).thenReturn(null);
        when(scheduleItem.getActualProviderData()).thenReturn(actualProviderData);

        dateUtilsMock.when(DateUtils::getCurrentDate).thenReturn(now);

        // When
        VideoStreamState state = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(scheduleItem);

        // Then
        assertThat(state).isEqualTo(VideoStreamState.STREAM);
    }

    @Test
    @DisplayName("Should get video stream state based on schedule item - leading time")
    void shouldGetVideoStreamStateBasedOnScheduleItemLeadingTime() {
        // Given
        ScheduleItemData actualProviderData = mock(ScheduleItemData.class);

        Date now = new Date();

        // Create a Date from 30 minutes before
        Date startDate = new Date(now.getTime() + 5 * 60 * 1000); // 5 minutes after
        when(actualProviderData.getStart()).thenReturn(startDate);

        Date endDate = new Date();
        endDate.setTime(now.getTime() + 30 * 60 * 1000); // 30 minutes
        when(actualProviderData.getEnd()).thenReturn(endDate);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.leadTime()).thenReturn(10 * 60); // 10 minutes (lead time in seconds)
        when(scheduleItem.trailTime()).thenReturn(null);
        when(scheduleItem.getActualProviderData()).thenReturn(actualProviderData);

        Date nowPlus10Minutes = new Date();
        nowPlus10Minutes.setTime(now.getTime() - 10 * 60 * 1000); // 10 minutes before

        dateUtilsMock.when(() -> DateUtils.shiftDateByField(any(Date.class), anyInt(), anyInt())).thenReturn(nowPlus10Minutes);
        dateUtilsMock.when(DateUtils::getCurrentDate).thenReturn(now);

        // When
        VideoStreamState state = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(scheduleItem);

        // Then
        assertThat(state).isEqualTo(VideoStreamState.STREAM);
    }

    @Test
    @DisplayName("Should get video stream state based on schedule item - trailing time")
    void shouldGetVideoStreamStateBasedOnScheduleItemTrailingTime() {
        // Given
        ScheduleItemData actualProviderData = mock(ScheduleItemData.class);

        Date now = new Date();

        // Create a Date from 30 minutes before
        Date startDate = new Date(now.getTime() - 5 * 60 * 1000); // 5 minutes before
        when(actualProviderData.getStart()).thenReturn(startDate);

        Date endDate = new Date();
        endDate.setTime(now.getTime() + 30 * 60 * 1000); // 30 minutes
        when(actualProviderData.getEnd()).thenReturn(endDate);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.leadTime()).thenReturn(null);
        when(scheduleItem.trailTime()).thenReturn(10 * 60); // 10 minutes (trailing time in seconds)
        when(scheduleItem.getActualProviderData()).thenReturn(actualProviderData);

        Date nowPlus10Minutes = new Date();
        nowPlus10Minutes.setTime(now.getTime() + 10 * 60 * 1000); // 10 minutes after

        dateUtilsMock.when(() -> DateUtils.shiftDateByField(any(Date.class), anyInt(), anyInt())).thenReturn(nowPlus10Minutes);
        dateUtilsMock.when(DateUtils::getCurrentDate).thenReturn(now);

        // When
        VideoStreamState state = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(scheduleItem);

        // Then
        assertThat(state).isEqualTo(VideoStreamState.STREAM);
    }

    @Test
    @DisplayName("Should return stream finished when date is after stream end time")
    void shouldReturnStreamFinishedWhenDateIsAfterStreamEndTime() {
        // Given
        ScheduleItemData actualProviderData = mock(ScheduleItemData.class);

        Date now = new Date();

        // Create a Date from 30 minutes before
        Date startDate = new Date(now.getTime() - 60 * 60 * 1000); // 1 hour before
        when(actualProviderData.getStart()).thenReturn(startDate);

        Date endDate = new Date();
        endDate.setTime(now.getTime() - 30 * 60 * 1000); // 30 minutes before
        when(actualProviderData.getEnd()).thenReturn(endDate);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.leadTime()).thenReturn(null);
        when(scheduleItem.trailTime()).thenReturn(null);
        when(scheduleItem.getActualProviderData()).thenReturn(actualProviderData);

        dateUtilsMock.when(DateUtils::getCurrentDate).thenReturn(now);

        // When
        VideoStreamState state = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(scheduleItem);

        // Then
        assertThat(state).isEqualTo(VideoStreamState.FINISHED);
    }

    @Test
    @DisplayName("Should return stream not started when date is before stream start time")
    void shouldReturnStreamNotStartedWhenDateIsBeforeStreamStartTime() {
        // Given
        ScheduleItemData actualProviderData = mock(ScheduleItemData.class);

        Date now = new Date();

        // Create a Date from 30 minutes before
        Date startDate = new Date(now.getTime() + 30 * 60 * 1000); // 30 minutes later
        when(actualProviderData.getStart()).thenReturn(startDate);

        Date endDate = new Date();
        endDate.setTime(now.getTime() + 60 * 60 * 1000); // 1 hour later
        when(actualProviderData.getEnd()).thenReturn(endDate);

        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        when(scheduleItem.leadTime()).thenReturn(null);
        when(scheduleItem.trailTime()).thenReturn(null);
        when(scheduleItem.getActualProviderData()).thenReturn(actualProviderData);

        dateUtilsMock.when(DateUtils::getCurrentDate).thenReturn(now);

        // When
        VideoStreamState state = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(scheduleItem);

        // Then
        assertThat(state).isEqualTo(VideoStreamState.NOT_STARTED);
    }

    @Test
    @DisplayName("Should throw exception if stream not started")
    void shouldThrowExceptionIfStreamNotStarted() {
        // Given
        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);

        // When & Then
        assertThatThrownBy(() -> scheduleItemService.checkIsCurrentlyShowingAndThrow(VideoStreamState.NOT_STARTED, 123L, context, 1))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_STARTED);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should throw exception if stream is finished")
    void shouldThrowExceptionIfStreamIsFinished() {
        // Given
        User user = mock(User.class);

        RequestContext context = mock(RequestContext.class);

        // When & Then
        assertThatThrownBy(() -> scheduleItemService.checkIsCurrentlyShowingAndThrow(VideoStreamState.FINISHED, 123L, context, 1))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_HAS_ENDED);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should validate if item is watch and bet supported")
    void shouldValidateIfItemIsWatchAndBetSupported() {
        // Given
        ScheduleItem scheduleItem = mock(ScheduleItem.class);
        ScheduleItemData providerData = mock(ScheduleItemData.class);

        when(providerData.getVenue()).thenReturn("TEST");
        when(scheduleItem.providerData()).thenReturn(providerData);

        when(configurationItemsPort.findProviderWatchAndBetVenues(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn("ANYTHING,TEST,RANDOM");

        // When
        boolean isSupported = scheduleItemService.isItemWatchAndBetSupported(scheduleItem);

        // Then
        assertThat(isSupported).isTrue();
    }

}
