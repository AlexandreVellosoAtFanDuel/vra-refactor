package com.betfair.video.api.domain.dto.valueobject;

public enum ImportStatus {

    NEW('N'),
    UPDATED('U'),
    DELETED('D'),
    IGNORED('I'),
    OVERRIDDEN('O');

    private final Character status;

    ImportStatus(Character status) {
        this.status = status;
    }

}
