package com.betfair.video.domain.dto.entity;

import com.betfair.video.domain.dto.valueobject.ImportStatus;

import java.io.Serializable;
import java.util.Date;

public record ScheduleItemMapping(
        ScheduleItemMappingKey scheduleItemMappingKey,
        String mappingDescription,
        String exchangeRaceId,
        String rampId,
        String meetingName,
        String venueName,
        String winMarketName,
        Character mappingSt,
        Character approvalSt,
        Character matchingTypeId,
        Date facetStartTime,
        Integer specialLogicMatchedItemScore,
        Integer editDistanceMatchedItemScore,
        String usedMatchers,
        Integer totalMatchedItemScore,
        Character manuallyApprovedFlag,
        Date createdDate,
        AuditItem audit
) implements Serializable {

    public ImportStatus mappingStatus() {
        return ImportStatus.fromValue(String.valueOf(this.mappingSt));
    }

}
