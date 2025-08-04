package com.betfair.video.api.infrastructure.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseDetailDto(
        VideoApiExceptionDto VideoAPIException,
        @JsonProperty("exceptionname")
        String exceptionName
) {
}
