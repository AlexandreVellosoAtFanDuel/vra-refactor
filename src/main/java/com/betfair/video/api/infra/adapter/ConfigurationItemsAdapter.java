package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationItemsAdapter implements ConfigurationItemsPort {

    @Override
    public boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId) {
        return true;
    }

}
