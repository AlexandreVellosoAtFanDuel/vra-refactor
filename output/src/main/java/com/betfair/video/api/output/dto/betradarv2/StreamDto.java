package com.betfair.video.api.output.dto.betradarv2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StreamDto(
        String id,
        BaseDto product,
        BaseDto distribution,
        @JsonProperty("start_time")
        String startTime,
        @JsonProperty("end_time")
        String endTime,
        @JsonProperty("stream_status")
        BaseDto streamStatus,
        List<BaseDto> languages,
        @JsonProperty("geo_restrictions")
        List<GeoRestrictionDto> geoRestrictions
) {
}
