package com.betfair.video.api.infra.input.rest.dto;

public record PlayerControlParamsDto(
        String widgetName,
        String sportName,
        String matchId,
        String streamFormat
) {
}
