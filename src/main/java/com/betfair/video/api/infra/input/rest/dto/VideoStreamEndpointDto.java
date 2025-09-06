package com.betfair.video.api.infra.input.rest.dto;

import java.util.Map;

public record VideoStreamEndpointDto(
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
