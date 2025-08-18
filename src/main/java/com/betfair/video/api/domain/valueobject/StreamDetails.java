package com.betfair.video.api.domain.valueobject;

import java.util.Map;

public record StreamDetails(
        String endpoint,
        VideoQuality quality,
        Map<StreamDetailsParamEnum, String> params
) {
}
