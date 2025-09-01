package com.betfair.video.api.domain.valueobject;

import java.util.Objects;

public enum ExternalIdSource {

    BETFAIR_EVENT(1, "BETFAIR_EVENT", 7);

    private final Integer source;
    private final String externalIdDescription;
    private final Integer providerId;

    ExternalIdSource(Integer source, String externalIdDescription, Integer providerId) {
        this.source = source;
        this.externalIdDescription = externalIdDescription;
        this.providerId = providerId;
    }

    public static ExternalIdSource fromExternalIdSource(String externalIdSource) {
        for (ExternalIdSource source : ExternalIdSource.values()) {
            if (Objects.equals(source.source, Integer.valueOf(externalIdSource))) {
                return source;
            }
        }

        throw new IllegalArgumentException("Unknown external ID source: " + externalIdSource);
    }

    public String getExternalIdDescription() {
        return this.externalIdDescription;
    }

    public Integer getProviderId() {
        return providerId;
    }
}
