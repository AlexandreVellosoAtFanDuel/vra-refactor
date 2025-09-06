package com.betfair.video.api.infra.input.rest.dto;

public record SizeRestrictionsDto(
        boolean fullScreenAllowed,
        boolean airPlayAllowed,
        String aspectRatio,
        int widthMax,
        int widthDefault
) {
}
