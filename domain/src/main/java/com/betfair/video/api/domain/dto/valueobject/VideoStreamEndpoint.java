package com.betfair.video.api.domain.dto.valueobject;

import java.util.Map;

public record VideoStreamEndpoint(
        Integer videoFormat,
        String videoQuality,
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
