package com.betfair.video.domain.port.output;

import com.betfair.video.domain.dto.entity.ConfigurationItem;
import com.betfair.video.domain.dto.entity.ConfigurationType;
import com.betfair.video.domain.dto.entity.Provider;
import com.betfair.video.domain.dto.entity.TypeStream;
import com.betfair.video.domain.dto.valueobject.StreamingFormat;

import java.util.Map;

public interface ConfigurationItemsPort {

    boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId);

    Map<ConfigurationType, String> getSizeRestrictions(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    ConfigurationItem findVideoPlayerConfig(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    String getDefaultBufferingInterval(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    String getDefaultVideoQuality(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    String findProviderWatchAndBetVenues(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4);

    StreamingFormat findPreferredStreamingFormat(Provider provider, Integer integer, Integer integer1, Integer integer2, Integer integer3);

}
