package com.betfair.video.api.infra.input.rest.dto.cro;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CROErrorDetail(
        @JsonProperty("SessionManagementException")
        CROSessionManagementException sessionManagementException,

        @JsonProperty("exceptionname")
        String exceptionName
) {
}