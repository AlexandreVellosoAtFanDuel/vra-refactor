package com.betfair.video.api.domain.dto.entity;

public record ProviderEventKey(
        Integer providerId,
        String primaryId,
        String secondaryId
) {
}
