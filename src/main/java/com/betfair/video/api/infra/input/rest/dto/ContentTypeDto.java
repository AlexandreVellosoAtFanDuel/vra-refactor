package com.betfair.video.api.infra.input.rest.dto;

public enum ContentTypeDto {

    VIZ("VIZ"),
    VID("VID"),
    PRE_VID("PRE_VID"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    ContentTypeDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
