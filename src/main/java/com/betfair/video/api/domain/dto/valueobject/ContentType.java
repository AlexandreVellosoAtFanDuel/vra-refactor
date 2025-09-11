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

    public String getValue() {
        return value;
    }

    public static ContentType fromValue(String v) {
        for (ContentType c : ContentType.values()) {
            if (c.value != null && c.value.equals(v)) {
                return c;
            }
        }

        return UNRECOGNIZED_VALUE;
    }

}
