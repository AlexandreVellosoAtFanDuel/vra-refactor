package com.betfair.video.api.domain.dto.valueobject;

import java.util.Map;

public record StreamDetails(
        String endpoint,
        VideoQuality quality,
        Map<String, String> params
) {
}
