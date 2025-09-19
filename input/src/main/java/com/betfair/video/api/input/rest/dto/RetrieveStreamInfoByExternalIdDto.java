package com.betfair.video.api.input.rest.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public record RetrieveStreamInfoByExternalIdDto(
        @RequestParam("externalIdSource")
        String externalIdSource,
        @RequestParam("externalId")
        String externalId,
        @RequestParam("channelTypeId")
        Integer channelTypeId,
        @RequestParam(value = "channelSubTypeIds", required = false)
        List<Integer> channelSubTypeIds,
        @RequestParam(value = "contentType", required = false)
        ContentTypeDto contentType,
        @RequestParam(value = "includeMetadata", required = false)
        Boolean includeMetadata,
        @RequestParam(value = "mobileDeviceId", required = false)
        Integer mobileDeviceId,
        @RequestParam(value = "mobileOsVersion", required = false)
        String mobileOsVersion,
        @RequestParam(value = "mobileScreenDensityDpi", required = false)
        Integer mobileScreenDensityDpi,
        @RequestParam(value = "videoQuality", required = false)
        VideoQualityDto videoQuality,
        @RequestParam(value = "commentaryLanguage", required = false)
        String commentaryLanguage,
        @RequestParam(value = "providerId", required = false)
        Integer providerId,
        @RequestParam(value = "providerParams", required = false)
        String providerParams
) {
}
