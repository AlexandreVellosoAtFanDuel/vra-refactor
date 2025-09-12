package com.betfair.video.input.rest.dto;

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

    public static ContentTypeDto fromValue(String value) {
        for (ContentTypeDto type : ContentTypeDto.values()) {
            if (type.value != null && type.value.equals(value)) {
                return type;
            }
        }

        return UNRECOGNIZED_VALUE;
    }

}
