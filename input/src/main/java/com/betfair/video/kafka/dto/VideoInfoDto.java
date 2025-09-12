package com.betfair.video.kafka.dto;

import java.util.List;

public record VideoInfoDto(
        Character approvalStatus,
        String importStatus,
        Character mappingApprovalStatus,
        Character mappingStatus,
        String commentaryLanguages,
        Boolean reviewStatus,
        String rampId,
        Integer channelSubTypeId,
        String providerEventId,
        String providerEventName,
        String providerVenue,
        String providerCompetition,
        Integer modifiedBySystemId,
        String modifiedByUser,
        Long modifiedDate,
        String modifiedByIp,
        Long mappingCreatedDate,
        Character mappingInexactManuallyApproved,
        Integer leadTime,
        Integer trailTime,
        List<ScheduleItemNameLocalizedDto> localisedEventNames
) {
}
