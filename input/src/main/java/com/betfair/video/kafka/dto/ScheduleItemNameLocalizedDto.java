package com.betfair.video.kafka.dto;

public record ScheduleItemNameLocalizedDto(
        Long videoItemId,
        String locale,
        String localizedEventName,
        AuditDto audit
) {
}
