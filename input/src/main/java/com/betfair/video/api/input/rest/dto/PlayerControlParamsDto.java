package com.betfair.video.api.input.rest.dto;

public record PlayerControlParamsDto(
        String widgetName,
        String sportName,
        String matchId,
        String streamFormat
) {
}
