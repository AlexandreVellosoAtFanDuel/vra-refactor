package com.betfair.video.api.input.kafka.dto;

import java.util.List;

public record ScheduleVideoDto(
        Integer sportId,
        List<EventDto> events
) {
}
