package com.betfair.video.domain.dto.valueobject;

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
        if (subdivisionsCodes == null || subdivisionsCodes.isEmpty()) {
            return null;
        }

        return ISO_3166_2_FORMATTER.apply(countryCode, subdivisionsCodes.getLast());
    }

}
