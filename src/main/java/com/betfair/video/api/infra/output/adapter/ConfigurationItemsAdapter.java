package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.dto.entity.ConfigurationItem;
import com.betfair.video.api.domain.dto.entity.ConfigurationType;
import com.betfair.video.api.domain.dto.entity.Provider;
import com.betfair.video.api.domain.dto.entity.TypeSport;
import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.valueobject.StreamingFormat;
import com.betfair.video.api.domain.dto.search.ConfigurationSearchKey;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigurationItemsAdapter implements ConfigurationItemsPort, RefreshMapCache<ConfigurationSearchKey, ConfigurationItem> {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationItemsAdapter.class);

    private final IMap<ConfigurationSearchKey, ConfigurationItem> configurationItemsMap;

    public ConfigurationItemsAdapter(IMap<ConfigurationSearchKey, ConfigurationItem> configurationItemsMap) {
        this.configurationItemsMap = configurationItemsMap;
    }

    @Override
    public boolean isStreamTypeAllowed(Integer providerId, Integer videoChannelType, Integer betfairSportsType, TypeStream typeStream, Integer brandId) {
        ConfigurationItem value = find(ConfigurationType.STREAM_TYPE_ENABLED, providerId, videoChannelType, betfairSportsType != null ? betfairSportsType : -1, -1, typeStream.getId(), brandId);
        return value != null && Boolean.parseBoolean(value.configValue());
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
        return new ConfigurationItem(
                null,
                null,
                null,
                null,
                null,
                null,
                "hls::{'maxBufferLength':30,'maxBufferSize':60000000,'maxMaxBufferLength':600,'liveSyncDurationCount':2,'liveMaxLatencyDurationCount':'3','abrEwmaFastLive':3.0,'abrEwmaSlowLive':9.0}--rtmp::{'maxBufferLength':1,'maxBufferSize':1,'maxMaxBufferLength':1,'liveSyncDurationCount':1,'liveMaxLatencyDurationCount':1,'abrEwmaFastLive':1,'abrEwmaSlowLive':1}",
                null,
                null
        );
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
        // TODO: Fetch from configuration
        return null;
    }

    @Override
    public StreamingFormat findPreferredStreamingFormat(Provider provider, Integer integer, Integer integer1, Integer integer2, Integer integer3) {
        // TODO: Fetch from configuration
        return StreamingFormat.HLS;
    }

    private ConfigurationItem find(ConfigurationType configType, Integer providerId, Integer channelType, Integer sportType, Integer mappingProviderId, Integer streamType, Integer brandId) {
        if (configType == null) {
            throw new IllegalArgumentException("Config Type must be specified");
        }

        ConfigurationSearchKey key = new ConfigurationSearchKey(
                configType,
                providerId != null ? providerId : Provider.NULL.getId(),
                channelType != null ? channelType : TypeStream.NULL.getId(),
                sportType != null ? sportType : TypeSport.NULL.getSportId(),
                mappingProviderId != null ? mappingProviderId : Provider.NULL.getId(),
                false,
                streamType != null ? streamType : TypeStream.NULL.getId(),
                brandId != null ? brandId : -1
        );

        return configurationItemsMap.get(key);
    }

    @Override
    public void insertItemsToCache(Map<ConfigurationSearchKey, ConfigurationItem> newItems) {
        if (configurationItemsMap.isEmpty()) {
            logger.info("Revalidating configuration cache with {} items", newItems.size());
            configurationItemsMap.putAll(newItems);
        }
    }

}
