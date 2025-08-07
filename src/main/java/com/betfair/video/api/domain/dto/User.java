package com.betfair.video.api.domain.dto;

public record User(
        String userId,
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
