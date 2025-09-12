package com.betfair.video.domain.dto.valueobject;

import java.util.Map;

public record StreamDetails(
        String endpoint,
        VideoQuality quality,
        Map<String, String> params
) {
}
