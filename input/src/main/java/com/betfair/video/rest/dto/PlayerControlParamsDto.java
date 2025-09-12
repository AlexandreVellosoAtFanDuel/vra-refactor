package com.betfair.video.rest.dto;

public record PlayerControlParamsDto(
        String widgetName,
        String sportName,
        String matchId,
        String streamFormat
) {
}
