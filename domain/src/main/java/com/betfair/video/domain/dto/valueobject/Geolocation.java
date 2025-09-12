package com.betfair.video.domain.dto.valueobject;

public record Geolocation(
        String countryCode,
        String subDivisionCode,
        Integer dmaId,
        boolean isSuspect
) {
}
