package com.betfair.video.api.domain.dto.valueobject;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public record CountryAndSubdivisions(
        String countryCode,
        List<String> subdivisionsCodes,
        Map<String, String> metaAttributes
) {

    private static final BiFunction<String, String, String> ISO_3166_2_FORMATTER = (countryCode, subdivisionCode) -> countryCode + "-" + subdivisionCode;

    public String getRegionCode() {
        return !CollectionUtils.isEmpty(this.subdivisionsCodes) ? ISO_3166_2_FORMATTER.apply(this.countryCode, this.subdivisionsCodes.getLast()) : null;
    }

}
