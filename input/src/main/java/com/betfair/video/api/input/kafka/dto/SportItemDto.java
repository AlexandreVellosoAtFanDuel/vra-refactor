package com.betfair.video.api.input.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SportItemDto(
        @JsonProperty("@type")
        String type,
        Integer id,
        String description
) {
}
