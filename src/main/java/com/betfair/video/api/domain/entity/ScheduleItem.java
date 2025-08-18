package com.betfair.video.api.domain.entity;

import com.betfair.video.api.domain.mapper.ScheduleItemMapper;

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
        Character importSt,
        Character approvalSt,
        Boolean reviewStatus,
        Integer streamTypeId,
        Boolean isExtractedFromMultiMatchStream,
        java.util.Date createdDate,
        Integer brandId,
        ScheduleItemData providerData,
        ScheduleItemData overriddenData,
        Set<ScheduleItemMapper> mappings
) {
    public ScheduleItemData getActualProviderData() {
        return null;
    }
}
