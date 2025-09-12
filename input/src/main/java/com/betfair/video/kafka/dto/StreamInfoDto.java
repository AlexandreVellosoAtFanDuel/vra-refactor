package com.betfair.video.kafka.dto;

import java.util.List;

public record StreamInfoDto(
        Long desktopVideoId,
        VideoInfoDto desktopVideoInfo,
        String providerSportsType,
        Long mobileVideoId,
        VideoInfoDto mobileVideoInfo,
        String streamType,
        Long streamTime,
        Long streamEndTime,
        BBVType bbvType,
        Double bbvFunding,
        BBVRequiredStakes bbvStakes,
        Integer providerId,
        List<String> geoblocked,
        Double aspectRatio,
        Integer maxWidthPixel
) {
}
