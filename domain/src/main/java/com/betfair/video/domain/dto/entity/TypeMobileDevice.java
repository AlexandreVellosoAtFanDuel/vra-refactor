package com.betfair.video.domain.dto.entity;

public enum TypeMobileDevice {

    NULL(-1),
    ANDROID_TABLET(1),
    ANDROID_PHONE(2),
    IOS_TABLET(3),
    IOS_PHONE(4);

    private final Integer id;

    TypeMobileDevice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
