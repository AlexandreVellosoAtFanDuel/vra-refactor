package com.betfair.video.api.domain.valueobject;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum VideoQuality {

    VERY_LOW("VERY_LOW"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    private static final Set<VideoQuality> validValues = Collections.unmodifiableSet(EnumSet.complementOf(EnumSet.of(VideoQuality.UNRECOGNIZED_VALUE)));

    VideoQuality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VideoQuality fromValue(String s) {
        for (VideoQuality v : validValues) {
            if (v.value != null && v.value.equals(s)) {
                return v;
            }
        }

        return UNRECOGNIZED_VALUE;
    }

    public static boolean isValidValue(String s) {
        for (VideoQuality videoQuality : validValues) {
            if (videoQuality.value != null && videoQuality.value.equals(s)) {
                return true;
            }
        }

        return false;
    }
}
