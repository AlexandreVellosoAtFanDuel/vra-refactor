package com.betfair.video.api.domain.entity;

public enum TypeSport {

    NULL(-1, "Other sports types"),
    FOOTBALL(1, "Football"),
    TENNIS(2, "Tennis"),
    GOLF(3, "Golf"),
    AMERICAN_FOOTBALL(6423, "American Football"),
    BASEBALL(7511, "Baseball"),
    BASKETBALL(7522, "Basketball"),
    HOCKEY(7523, "Hockey"),
    ICE_HOCKEY(7524, "Ice Hockey"),
    TABLE_TENNIS(2593174, "Table Tennis"),
    VIRTUAL_SPORT(27438978, "Virtual Sport"),
    ESPORTS(27454571, "Esports");

    private final Integer sportId;

    private final String description;

    TypeSport(Integer sportId, String description) {
        this.sportId = sportId;
        this.description = description;
    }

    public static TypeSport getById(Integer id) {
        for (TypeSport typeSport : values()) {
            if (typeSport.sportId.equals(id)) {
                return typeSport;
            }
        }

        return NULL;
    }

    public String getDescription() {
        return description;
    }
}
