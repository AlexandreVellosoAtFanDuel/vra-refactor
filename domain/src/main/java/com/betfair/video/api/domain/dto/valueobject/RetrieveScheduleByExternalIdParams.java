package com.betfair.video.api.domain.dto.valueobject;

import com.betfair.video.api.domain.dto.entity.RequestContext;

import java.util.List;

public record RetrieveScheduleByExternalIdParams(
        RequestContext context,
        String externalIdSource,
        String externalId,
        Integer channelTypeId,
        List<Integer> channelSubTypeIds,
        Integer mobileDeviceId,
        String mobileOsVersion,
        Integer mobileScreenDensityDpi,
        VideoQuality videoQuality,
        String commentaryLanguage,
        Integer providerId,
        ContentType contentType,
        Boolean includeMetadata,
        String providerParams
) {
}
