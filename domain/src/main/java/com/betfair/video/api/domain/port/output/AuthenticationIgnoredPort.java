package com.betfair.video.api.domain.port.output;

public interface AuthenticationIgnoredPort {

    boolean isProviderInList(Integer providerId, Integer videoChannelType);

}
