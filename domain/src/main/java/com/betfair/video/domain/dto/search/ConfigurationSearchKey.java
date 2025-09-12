package com.betfair.video.domain.dto.search;

import com.betfair.video.domain.dto.entity.ConfigurationType;

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
