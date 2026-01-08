package com.betfair.video.api.domain.dto.valueobject;

public enum StreamingFormat {

    RTMP("rtmp"),
    HLS("hls"),
    MPEG_DASH("mpeg-dash"),
    CMAF_DASH("cmaf-dash"),
    MP4("mp4"),
    WS("webrtc"),
    IFRAME("iframe"),
    LL_HLS("ll-hls");

    private final String value;

    StreamingFormat(String value) {
        this.value = value;
    }

    public static StreamingFormat fromValue(String s) {
        for (StreamingFormat format : StreamingFormat.values()) {
            if (format.value.equalsIgnoreCase(s)) {
                return format;
            }
        }

        throw new IllegalArgumentException("Unknown streaming format: " + s);
    }

    public String getValue() {
        return value;
    }
}
