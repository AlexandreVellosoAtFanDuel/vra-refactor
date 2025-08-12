package com.betfair.video.api.application.dto;

public enum ContentTypeDto {

    VIZ("VIZ"),
    VID("VID"),
    PRE_VID("PRE_VID"),
    UNRECOGNIZED_VALUE(null);

    private String value;

    ContentTypeDto(String value) {
        this.value = value;
    }
}
