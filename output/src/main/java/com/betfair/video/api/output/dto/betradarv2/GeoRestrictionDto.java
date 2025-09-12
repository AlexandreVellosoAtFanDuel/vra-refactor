package com.betfair.video.api.output.dto.betradarv2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GeoRestrictionDto(
        @JsonProperty("device_category")
        BaseDto deviceCategory,
        @JsonProperty("country_iso_alpha2_codes")
        String countryIsoAlpha2Codes,
        @JsonProperty("country_subdivision_iso_codes")
        Map<String, String> countrySubdivisionIsoCodes
) {
}
