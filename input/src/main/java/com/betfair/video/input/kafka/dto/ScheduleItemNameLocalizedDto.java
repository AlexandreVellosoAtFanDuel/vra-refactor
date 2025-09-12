package com.betfair.video.input.kafka.dto;

public record ScheduleItemNameLocalizedDto(
        Long videoItemId,
        String locale,
        String localizedEventName,
        AuditDto audit
) {
}
