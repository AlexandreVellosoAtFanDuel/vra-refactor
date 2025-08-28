package com.betfair.video.api.domain.valueobject;

import java.util.Objects;

public enum ExternalIdSource {

    BETFAIR_EVENT(1, "BETFAIR_EVENT", 7),
    BETFAIR_MARKET(2, "BETFAIR_MARKET", 7),
    TIMEFORM(3, "TIMEFORM", 8),
    EXCHANGE_RACE(4, "EXCHANGE_RACE", 7),
    BETFAIR_VIDEO(5, "BETFAIR_VIDEO", 7),
    RAMP(6, "RAMP", 7);

    private Integer externalIdSource;
    private String externalIdDescription;
    private Integer providerId;

    ExternalIdSource(Integer externalIdSource, String externalIdDescription, Integer providerId) {
        this.externalIdSource = externalIdSource;
        this.externalIdDescription = externalIdDescription;
        this.providerId = providerId;
    }

    public static ExternalIdSource fromExternalIdSource(String externalIdSource) {
        for (ExternalIdSource source : ExternalIdSource.values()) {
            if (Objects.equals(source.externalIdSource, Integer.valueOf(externalIdSource))) {
                return source;
            }
        }

        throw new IllegalArgumentException("Unknown external ID source: " + externalIdSource);
    }

    public String getExternalIdDescription() {
        return this.externalIdDescription;
    }

    public Integer getExternalIdSource() {
        return externalIdSource;
    }

    public Integer getProviderId() {
        return providerId;
    }
}
