package com.betfair.video.kafka.dto;

import java.util.List;

public record ScheduleVideoDto(
        Integer sportId,
        List<EventDto> events
) {
}
