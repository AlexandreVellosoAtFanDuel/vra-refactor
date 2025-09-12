package com.betfair.video.api.domain.dto.valueobject;

public enum StreamDetailsParamEnum {
    STREAM_NAME_PARAM_NAME("streamName"),
    STREAM_FORMAT_PARAM_NAME("streamFormat"),
    PHENIX_TOKEN_NAME("phenixToken"),
    STREAM_START_TIME("streamStartTime"),
    WEBRTC_SUPERUSER("webrtcSuperUser"),
    PROVIDER_SPORT_TYPE("providerSportType"),
    HIGH_LATENCY_ICON_SIZE("highLatencyIconSize");

    private final String paramName;

    StreamDetailsParamEnum(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
