package com.betfair.video.domain.dto.entity;

import java.io.Serializable;

public record ConfigurationItem(
        String configType,
        Integer providerId,
        Integer channelType,
        Integer sportType,
        Integer mappingProviderId,
        Integer streamTypeId,
        String configValue,
        Integer brandId,
        AuditItem audit
) implements Serializable {
}
