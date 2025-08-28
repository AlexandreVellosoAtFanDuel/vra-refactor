package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.entity.ScheduleItemMappingKey;
import com.betfair.video.api.domain.valueobject.ImportStatus;

public record ScheduleItemMapper(
        String exchangeRaceId,
        String rampId,
        ScheduleItemMappingKey scheduleItemMappingKey,
        String mappingDescription,
        ImportStatus mappingStatus
) {
}
