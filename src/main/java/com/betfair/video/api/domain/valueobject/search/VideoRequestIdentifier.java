package com.betfair.video.api.domain.valueobject.search;

public record VideoRequestIdentifier(
        String videoId,
        String eventId,
        String marketId,
        String exchangeRaceId,
        String timeformRaceId,
        String rampId
) {
}
