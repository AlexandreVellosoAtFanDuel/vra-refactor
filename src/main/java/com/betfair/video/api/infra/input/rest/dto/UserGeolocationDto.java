package com.betfair.video.api.infra.input.rest.dto;

public record UserGeolocationDto(
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
