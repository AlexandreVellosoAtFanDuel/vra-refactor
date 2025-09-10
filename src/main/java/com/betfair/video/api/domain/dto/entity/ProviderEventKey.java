package com.betfair.video.api.domain.dto.entity;

import java.io.Serializable;

public record ProviderEventKey(
        Integer providerId,
        String primaryId,
        String secondaryId
) implements Serializable {
}
