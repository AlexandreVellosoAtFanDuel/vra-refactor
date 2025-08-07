package com.betfair.video.api.application.dto.cro;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CROSessionManagementException(
        @JsonProperty("errorCode")
        String errorCode,

        @JsonProperty("exceptionMessage")
        String exceptionMessage
) {
}