package com.betfair.video.kafka.dto;

import java.util.List;

public record ScheduleVideoMessageDto(
        Long msgId,
        Long publishTime,
        Long scheduleStartTime,
        Long scheduleEndTime,
        List<ScheduleVideoDto> schedules
) {
}
