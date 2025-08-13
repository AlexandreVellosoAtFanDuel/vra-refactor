package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.TypeStream;

public interface ConfigurationItemsPort {

    boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId);

}
