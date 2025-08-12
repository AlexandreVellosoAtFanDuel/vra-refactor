package com.betfair.video.api.domain.valueobject;

public record VideoStreamEndpoint(
        Integer videoFormat,
        String videoQuality,
        String videoEndpoint,
        String playerControlParams
) {
}
