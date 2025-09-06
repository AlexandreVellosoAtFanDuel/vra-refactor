package com.betfair.video.api.domain.dto.valueobject;

public enum ContentType {

    VIZ("VIZ"),
    VID("VID"),
    PRE_VID("PRE_VID"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

}
