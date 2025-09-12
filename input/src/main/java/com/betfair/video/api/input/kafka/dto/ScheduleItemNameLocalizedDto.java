package com.betfair.video.api.input.kafka.dto;

public record ScheduleItemNameLocalizedDto(
        Long videoItemId,
        String locale,
        String localizedEventName,
        AuditDto audit
) {
}
