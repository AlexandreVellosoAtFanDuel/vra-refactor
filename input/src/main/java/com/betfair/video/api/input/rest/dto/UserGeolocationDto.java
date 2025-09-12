package com.betfair.video.api.input.rest.dto;

public record UserGeolocationDto(
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
