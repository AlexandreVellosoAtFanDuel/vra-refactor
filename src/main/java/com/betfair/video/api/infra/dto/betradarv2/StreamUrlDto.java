package com.betfair.video.api.infra.dto.betradarv2;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StreamUrlDto(
        String url,
        @JsonProperty("stream_name")
        String streamName
) {
}
