package com.betfair.video.api.domain.entity;

public record ProviderEventKey(
        Integer providerId,
        String primaryId,
        String secondaryId
) {
}
