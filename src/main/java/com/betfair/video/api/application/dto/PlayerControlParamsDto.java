package com.betfair.video.api.application.dto;

public record PlayerControlParamsDto(
        String widgetName,
        String sportName,
        String matchId
) {
}
