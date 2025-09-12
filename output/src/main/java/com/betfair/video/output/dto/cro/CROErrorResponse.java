package com.betfair.video.output.dto.cro;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CROErrorResponse(
        @JsonProperty("faultcode")
        String faultCode,

        @JsonProperty("faultstring")
        String faultString,

        @JsonProperty("detail")
        CROErrorDetail detail
) {
}