package com.betfair.video.api.application.dto;

public record UserGeolocationDto(
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
