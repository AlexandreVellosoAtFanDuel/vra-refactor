package com.betfair.video.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDetailDto(
        @JsonProperty("VideoAPIException")
        VideoExceptionDto VideoException,
        String trace,
        String message,
        @JsonProperty("exceptionname")
        String exceptionName
) {
}
