package com.betfair.video.api.domain.valueobject;

public enum VideoQuality {

    VERY_LOW("VERY_LOW"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    VideoQuality(String value) {
        this.value = value;
    }

}
