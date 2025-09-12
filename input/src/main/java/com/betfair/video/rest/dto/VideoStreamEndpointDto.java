package com.betfair.video.rest.dto;

import java.util.Map;

public record VideoStreamEndpointDto(
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
