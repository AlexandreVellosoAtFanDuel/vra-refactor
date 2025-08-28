package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class ConfigurationItemsAdapter implements ConfigurationItemsPort {

    @Override
    public boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId) {
        return true;
    }

    @Override
    public Map<ConfigurationType, String> getSizeRestrictions(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        // TODO: Fetch from configuration
        return Map.of(
                ConfigurationType.SIZE_RESTRICTION_FULLSCREEN_ALLOWED, "false",
                ConfigurationType.SIZE_RESTRICTION_ASPECT_RATIO, "0.5625",
                ConfigurationType.SIZE_RESTRICTION_MAX_WIDTH, "1080",
                ConfigurationType.SIZE_RESTRICTION_DEAFULT_WIDTH, "480"
        );
    }

    @Override
    public ConfigurationItem findVideoPlayerConfig(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        // TODO: Fetch from configuration
        return new ConfigurationItem("hls::{'maxBufferLength':30,'maxBufferSize':60000000,'maxMaxBufferLength':600,'liveSyncDurationCount':2,'liveMaxLatencyDurationCount':'3','abrEwmaFastLive':3.0,'abrEwmaSlowLive':9.0}--rtmp::{'maxBufferLength':1,'maxBufferSize':1,'maxMaxBufferLength':1,'liveSyncDurationCount':1,'liveMaxLatencyDurationCount':1,'abrEwmaFastLive':1,'abrEwmaSlowLive':1}");
    }

    @Override
    public String getDefaultBufferingInterval(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        // TODO: Fetch from configuration
        return "1";
    }

    @Override
    public String getDefaultVideoQuality(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        // TODO: Fetch from configuration
        return "MEDIUM";
    }

    @Override
    public String findProviderWatchAndBetVenues(Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
        return null;
    }
}
