package com.betfair.video.input.rest.dto;

public record UserGeolocationDto(
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
