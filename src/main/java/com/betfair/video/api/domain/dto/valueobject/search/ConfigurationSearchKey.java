package com.betfair.video.api.domain.dto.valueobject.search;

import com.betfair.video.api.domain.dto.entity.ConfigurationType;

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
