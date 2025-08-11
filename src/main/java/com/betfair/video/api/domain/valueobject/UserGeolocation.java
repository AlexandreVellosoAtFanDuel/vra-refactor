package com.betfair.video.api.domain.valueobject;

public record UserGeolocation(
        String countryCode,
        String subDivisionCode,
        Integer dmaId,
        boolean isSuspect
) {
}
