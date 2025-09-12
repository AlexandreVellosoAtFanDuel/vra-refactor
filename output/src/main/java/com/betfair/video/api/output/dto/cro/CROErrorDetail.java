package com.betfair.video.api.output.dto.cro;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CROErrorDetail(
        @JsonProperty("SessionManagementException")
        CROSessionManagementException sessionManagementException,

        @JsonProperty("exceptionname")
        String exceptionName
) {
}