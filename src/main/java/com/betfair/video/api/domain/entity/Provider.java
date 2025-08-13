package com.betfair.video.api.domain.entity;

import java.util.Set;

public enum Provider {

    NULL(-1),
    RACING_UK_WEB_STREAMING(1),
    BETFAIR(7),
    TIMEFORM(8),
    AT_THE_RACES_WEB_STREAMING(16),
    ATR_RSA_WEB_STREAMING(18),
    BETGENIUS(19),
    SIS_TV(20),
    RCN(21),
    SPIN(22),
    ATR_GREYHOUNDS_STREAMING(24),
    ATR_OTHER(25),
    IMG(26),
    ATR_PMU(28),
    PERFORM_V2_RUK(29),
    PERFORM_V2_GREYHOUNDS_TV(30),
    PERFORM_V2_OTHER(31),
    PERFORM_V2_VISUALISATION(32),
    BETRADAR_V2(33),
    BETRADAR_V3(34),
    IMG_GOLF(36),
    PERFORM_MCC_RUK(37),
    PERFORM_MCC_VIDEO(38),
    PERFORM_MCC_VISUALISATION(39),
    GENIUS_SPORTS(40);

    public static final Set<Integer> ARCHIVED_VIDEO_PROVIDERS = Set.of(1, 21, 16);

    public static final Set<Integer> ATR_PROVIDERS = Set.of(18, 25, 24, 16, 28);

    private final int id;

    Provider(int id) {
        this.id = id;
    }
}
