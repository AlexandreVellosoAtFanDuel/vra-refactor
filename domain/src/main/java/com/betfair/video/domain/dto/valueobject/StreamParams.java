package com.betfair.video.domain.dto.valueobject;

public record StreamParams(
        String commentaryLanguage,
        VideoQuality videoQuality,
        Integer mobileDeviceTypeId,
        String mobileOsVersion,
        Integer mobileScreenDensityDpi,
        String providerParams,
        boolean skipCaching,
        Integer channelTypeId
) {
}
