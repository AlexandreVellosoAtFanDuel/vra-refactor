package com.betfair.video.domain.dto.search;

public record VideoRequestIdentifier(
        String videoId,
        String eventId,
        String marketId,
        String exchangeRaceId,
        String timeformRaceId,
        String rampId
) {
}
