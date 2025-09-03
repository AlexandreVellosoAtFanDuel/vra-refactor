package com.betfair.video.api.domain.valueobject.search;

import com.betfair.video.api.domain.entity.ConfigurationType;

public record ConfigurationSearchKey(
        ConfigurationType configType,
        Integer providerId,
        Integer channelType,
        Integer sportType,
        Integer mappingProviderId,
        Boolean ignoreSportType,
        Integer streamType,
        Integer brandId
) {
}
