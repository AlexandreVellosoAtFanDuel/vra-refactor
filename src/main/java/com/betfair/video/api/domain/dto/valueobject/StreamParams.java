package com.betfair.video.api.domain.dto.valueobject;

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
