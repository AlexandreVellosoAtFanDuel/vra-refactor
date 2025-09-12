package com.betfair.video.domain.dto.valueobject;

import java.io.Serializable;

public enum ImportStatus implements Serializable {

    NEW('N'),
    UPDATED('U'),
    DELETED('D'),
    IGNORED('I'),
    OVERRIDDEN('O');

    private final Character status;

    ImportStatus(Character status) {
        this.status = status;
    }

    public static ImportStatus fromValue(String s) {
        for (ImportStatus importStatus : ImportStatus.values()) {
            if (importStatus.status.equals(s.charAt(0))) {
                return importStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + s + " found");
    }

}
