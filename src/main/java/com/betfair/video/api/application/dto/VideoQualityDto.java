package com.betfair.video.api.application.dto;

public enum VideoQualityDto {

    VERY_LOW("VERY_LOW"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    VideoQualityDto(String value) {
        this.value = value;
    }
}
