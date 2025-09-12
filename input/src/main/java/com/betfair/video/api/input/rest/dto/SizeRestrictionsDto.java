package com.betfair.video.api.input.rest.dto;

public record SizeRestrictionsDto(
        boolean fullScreenAllowed,
        boolean airPlayAllowed,
        String aspectRatio,
        int widthMax,
        int widthDefault
) {
}
