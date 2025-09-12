package com.betfair.video.domain.dto.valueobject;

public record VideoStreamInfoDelegate(
        Long accountId,
        String timeformRaceId,
        VideoQuality defaultVideoQuality
) {
}
