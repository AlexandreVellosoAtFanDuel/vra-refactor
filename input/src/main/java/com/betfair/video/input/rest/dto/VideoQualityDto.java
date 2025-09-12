package com.betfair.video.input.rest.dto;

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

    public static VideoQualityDto fromValue(String value) {
        for (VideoQualityDto quality : VideoQualityDto.values()) {
            if (quality.value != null && quality.value.equals(value)) {
                return quality;
            }
        }

        return UNRECOGNIZED_VALUE;
    }
}
