package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigurationItemsAdapter implements ConfigurationItemsPort {

    @Override
    public boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId) {
        return true;
    }

    @Override
    public Map<ConfigurationType, String> getSizeRestrictions(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }

    @Override
    public ConfigurationItem findVideoPlayerConfig(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }

    @Override
    public String getDefaultBufferingInterval(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }

    @Override
    public String getDefaultVideoQuality(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }

    @Override
    public String findProviderWatchAndBetVenues(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }
}
