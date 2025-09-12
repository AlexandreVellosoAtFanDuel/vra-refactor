package com.betfair.video.domain.dto.valueobject;

import java.util.Map;

public record VideoStreamEndpoint(
        Integer videoFormat,
        String videoQuality,
        String videoEndpoint,
        Map<String, String> playerControlParams
) {
}
