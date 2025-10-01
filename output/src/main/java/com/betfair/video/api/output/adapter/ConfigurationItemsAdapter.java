package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.ConfigurationItem;
import com.betfair.video.api.domain.dto.entity.ConfigurationType;
import com.betfair.video.api.domain.dto.entity.Provider;
import com.betfair.video.api.domain.dto.entity.TypeSport;
import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.search.ConfigurationSearchKey;
import com.betfair.video.api.domain.dto.valueobject.StreamingFormat;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.RefreshMapCache;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
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
    public Map<ConfigurationType, String> getSizeRestrictions(Integer providerId, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        return this.getConfigItems(new ConfigurationType[]{ConfigurationType.SIZE_RESTRICTION_WIDTH_CENTIMETER, ConfigurationType.SIZE_RESTRICTION_WIDTH_PIXEL, ConfigurationType.SIZE_RESTRICTION_WIDTH_PERCENTAGE, ConfigurationType.SIZE_RESTRICTION_HEIGHT_CENTIMETER, ConfigurationType.SIZE_RESTRICTION_HEIGHT_PIXEL, ConfigurationType.SIZE_RESTRICTION_HEIGHT_PERCENTAGE, ConfigurationType.SIZE_RESTRICTION_FULLSCREEN_ALLOWED, ConfigurationType.SIZE_RESTRICTION_AIRPLAY_ALLOWED, ConfigurationType.SIZE_RESTRICTION_ASPECT_RATIO, ConfigurationType.SIZE_RESTRICTION_MAX_WIDTH, ConfigurationType.SIZE_RESTRICTION_DEAFULT_WIDTH}, providerId, channelType, sportType, streamTypeId, brandId);
    }

    @Override
    public ConfigurationItem findVideoPlayerConfig(Integer providerId, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        return this.find(ConfigurationType.VIDEO_PLAYER_CONFIG, providerId, channelType, sportType != null ? sportType : TypeSport.NULL.getSportId(), -1, streamTypeId, brandId);
    }

    @Override
    public String getDefaultBufferingInterval(Integer providerId, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        ConfigurationItem item = this.find(ConfigurationType.DEFAULT_BUFFERING_INTERVAL, providerId, channelType, sportType, -1, streamTypeId, brandId);
        return item != null ? item.configValue() : "";
    }

    @Override
    public String getDefaultVideoQuality(Integer providerId, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        ConfigurationItem item = this.find(ConfigurationType.DEFAULT_VIDEO_QUALITY, providerId, channelType, sportType, -1, streamTypeId, brandId);
        return item != null ? item.configValue() : "";
    }

    @Override
    public String findProviderWatchAndBetVenues(Integer providerId, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        ConfigurationItem value = this.find(ConfigurationType.PROVIDER_WATCH_AND_BET_VENUES, providerId, channelType, sportType, -1, streamTypeId, brandId);
        return value != null ? value.configValue() : null;
    }

    @Override
    public StreamingFormat findPreferredStreamingFormat(Provider provider, Integer channelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        ConfigurationItem value = this.find(ConfigurationType.PREFERRED_STREAMING_FORMAT, provider.getId(), channelType, sportType, -1, streamTypeId, brandId);
        return value != null && value.configValue() != null ? StreamingFormat.fromValue(value.configValue().trim()) : null;
    }

    @Override
    public String findProviderBlockedCountries(Integer providerId, Integer videoChannelType, Integer sportType, Integer streamTypeId, Integer brandId) {
        ConfigurationItem value = this.find(ConfigurationType.GEO_BLOCKING, providerId, videoChannelType, sportType, -1, streamTypeId, brandId);
        return value != null ? value.configValue() : null;
    }

    private Map<ConfigurationType, String> getConfigItems(ConfigurationType[] types, Integer providerId, Integer channelTypeId, Integer sportType, Integer streamTypeId, Integer brandId) {
        Map<ConfigurationType, String> result = new EnumMap<>(ConfigurationType.class);

        for (ConfigurationType configurationType : types) {
            ConfigurationItem item = this.find(configurationType, providerId, channelTypeId, sportType, -1, streamTypeId, brandId);
            if (item != null) {
                result.put(configurationType, item.configValue());
            }
        }

        return result;
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
