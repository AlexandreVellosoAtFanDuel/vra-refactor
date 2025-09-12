package com.betfair.video.api.output.dto.betradarv2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ContentDto(
        String id,
        String name,
        @JsonProperty("content_type")
        BaseDto contentType,
        @JsonProperty("content_variant")
        BaseDto contentVariant,
        @JsonProperty("is_main")
        Boolean isMain,
        List<StreamDto> streams
) {
}
