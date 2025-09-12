package com.betfair.video.input.rest.dto;

import java.util.Map;

public record VideoStreamEndpointDto(
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
