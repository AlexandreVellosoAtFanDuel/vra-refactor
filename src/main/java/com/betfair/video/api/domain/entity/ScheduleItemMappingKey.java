package com.betfair.video.api.domain.entity;

public record ScheduleItemMappingKey(
        String videoItemId,
        ProviderEventKey providerEventKey
) {
}
