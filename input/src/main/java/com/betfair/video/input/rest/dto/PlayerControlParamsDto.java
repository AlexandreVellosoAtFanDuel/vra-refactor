package com.betfair.video.input.rest.dto;

public record PlayerControlParamsDto(
        String widgetName,
        String sportName,
        String matchId,
        String streamFormat
) {
}
