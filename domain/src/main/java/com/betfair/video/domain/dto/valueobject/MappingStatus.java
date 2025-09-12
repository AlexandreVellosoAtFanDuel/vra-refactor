package com.betfair.video.domain.dto.valueobject;

public enum MappingStatus {

    NEW('N'),
    UPDATED('U'),
    DELETED('D'),
    IGNORED('I'),
    OVERRIDDEN('O');

    private final Character status;

    MappingStatus(Character c) {
        this.status = c;
    }

    public static MappingStatus fromValue(Character value) {
        if (value == null) {
            return null;
        }

        for (MappingStatus mappingStatus : MappingStatus.values()) {
            if (mappingStatus.status.equals(value)) {
                return mappingStatus;
            }
        }

        return null;
    }

    public char getStatus() {
        return this.status;
    }

}
