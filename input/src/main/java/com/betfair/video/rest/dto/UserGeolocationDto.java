package com.betfair.video.rest.dto;

public record UserGeolocationDto(
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
