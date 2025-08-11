package com.betfair.video.api.domain.valueobject;

public record Geolocation(
        String countryCode,
        String subDivisionCode,
        Integer dmaId,
        boolean isSuspect
) {
}
