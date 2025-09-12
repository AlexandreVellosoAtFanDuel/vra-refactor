package com.betfair.video.domain.dto.entity;

public enum Provider {

    NULL(-1),
    BETGENIUS(19),
    IMG(26),
    PERFORM_V2_OTHER(31),
    PERFORM_V2_VISUALISATION(32),
    BETRADAR_V2(33),
    BETRADAR_V3(34),
    IMG_GOLF(36),
    PERFORM_MCC_RUK(37),
    PERFORM_MCC_VIDEO(38),
    PERFORM_MCC_VISUALISATION(39),
    GENIUS_SPORTS(40);

    private final int id;

    Provider(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
