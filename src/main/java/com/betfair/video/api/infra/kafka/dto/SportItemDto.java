package com.betfair.video.api.infra.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SportItemDto(
        @JsonProperty("@type")
        String type,
        Integer id,
        String description
) {
}
