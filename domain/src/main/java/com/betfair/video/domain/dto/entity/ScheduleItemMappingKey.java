package com.betfair.video.domain.dto.entity;

import java.io.Serializable;

public record ScheduleItemMappingKey(
        String videoItemId,
        ProviderEventKey providerEventKey
) implements Serializable {
}
