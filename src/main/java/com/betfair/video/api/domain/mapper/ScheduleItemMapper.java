package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.dto.entity.ScheduleItemMappingKey;
import com.betfair.video.api.domain.dto.valueobject.ImportStatus;

public record ScheduleItemMapper(
        String exchangeRaceId,
        String rampId,
        ScheduleItemMappingKey scheduleItemMappingKey,
        String mappingDescription,
        ImportStatus mappingStatus
) {
}
