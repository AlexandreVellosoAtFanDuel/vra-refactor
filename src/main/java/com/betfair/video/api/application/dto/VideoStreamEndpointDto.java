package com.betfair.video.api.application.dto;

import java.util.Map;

public record VideoStreamEndpointDto(
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
