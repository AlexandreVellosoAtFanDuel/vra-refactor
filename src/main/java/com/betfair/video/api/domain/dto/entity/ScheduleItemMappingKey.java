package com.betfair.video.api.domain.dto.entity;

public record ScheduleItemMappingKey(
        String videoItemId,
        ProviderEventKey providerEventKey
) {
}
