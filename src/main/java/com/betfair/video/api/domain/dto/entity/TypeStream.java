package com.betfair.video.api.domain.dto.entity;

import java.util.Set;

public enum TypeStream {

    NULL(-1),
    VID(1),
    VIZ(2),
    PRE_VID(3);

    public static final Set<Integer> REGULAR_STREAM_TYPES = Set.of(VID.getId(), VIZ.getId());

    private final int id;

    TypeStream(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TypeStream fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (TypeStream typeStream : TypeStream.values()) {
            if (typeStream.name().equalsIgnoreCase(value)) {
                return typeStream;
            }
        }

        return null;
    }

}
