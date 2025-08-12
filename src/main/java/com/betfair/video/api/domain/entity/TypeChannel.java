package com.betfair.video.api.domain.entity;

public enum TypeChannel {

    NULL(-1),
    WEB(1),
    MOBILE(2),
    ARCHIVE(3);

    private final Integer id;

    TypeChannel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
