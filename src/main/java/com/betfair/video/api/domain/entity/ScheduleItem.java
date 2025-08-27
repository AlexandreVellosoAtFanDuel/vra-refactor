package com.betfair.video.api.domain.entity;

import com.betfair.video.api.domain.mapper.ScheduleItemMapper;
import com.betfair.video.api.domain.valueobject.ImportStatus;

import java.util.Date;
import java.util.Set;

public record ScheduleItem(
        Long videoItemId,
        Integer providerId,
        String providerEventId,
        String providerSportsType,
        String providerLanguage,
        Boolean providerChargeableFlag,
        String providerStreamingUrl,
        Boolean providerFinished,
        Integer betfairSportsType,
        Integer leadTime,
        Integer trailTime,
        Integer videoChannelType,
        Integer videoChanelSubType,
        ImportStatus importStatus,
        Character approvalStatus,
        Boolean reviewStatus,
        Integer streamTypeId,
        Boolean isExtractedFromMultiMatchStream,
        Date createdDate,
        Integer brandId,
        ScheduleItemData providerData,
        ScheduleItemData overriddenData,
        Set<ScheduleItemMapper> mappings
) {
    public ScheduleItemData getActualProviderData() {
        if (this.providerData == null) {
            return null;
        }

        ScheduleItemData result = this.providerData.clone();
        result.override(this.overriddenData);
        return result;
    }
}
