package com.betfair.video.api.infra.input.rest.dto;

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

    public String getValue() {
        return value;
    }
}
