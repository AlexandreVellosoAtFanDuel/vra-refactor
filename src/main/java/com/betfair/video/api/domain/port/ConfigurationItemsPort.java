package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.TypeStream;

import java.util.Map;

public interface ConfigurationItemsPort {

    boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId);

    Map<ConfigurationType, String> getSizeRestrictions(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    ConfigurationItem findVideoPlayerConfig(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    String getDefaultBufferingInterval(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    String getDefaultVideoQuality(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

}
