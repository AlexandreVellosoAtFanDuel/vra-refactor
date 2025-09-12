package com.betfair.video.api.domain.dto.valueobject;

public record VideoStreamInfoDelegate(
        Long accountId,
        String timeformRaceId,
        VideoQuality defaultVideoQuality
) {
}
