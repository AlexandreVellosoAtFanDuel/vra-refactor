package com.betfair.video.api.application.dto;

public record SizeRestrictionsDto(
        boolean fullScreenAllowed,
        boolean airPlayAllowed,
        String aspectRatio,
        int widthMax,
        int widthDefault
) {
}
