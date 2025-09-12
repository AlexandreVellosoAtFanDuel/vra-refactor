package com.betfair.video.api.domain.dto.valueobject;

public record Geolocation(
        String countryCode,
        String subDivisionCode,
        Integer dmaId,
        boolean isSuspect
) {
}
