package com.betfair.video.api.infra.consumer;

import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.valueobject.search.ConfigurationSearchKey;
import com.betfair.video.api.infra.adapter.ConfigurationItemsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigurationItemCacheManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationItemCacheManager.class);

    private final ConfigurationItemsAdapter configurationItemsAdapter;

    public ConfigurationItemCacheManager(ConfigurationItemsAdapter configurationItemsAdapter) {
        this.configurationItemsAdapter = configurationItemsAdapter;
    }

    @Override
    public void run() {
        logger.info("Revalidating configuration item cache...");
        revalidateCache();
    }

    public void revalidateCache() {
        Map<ConfigurationSearchKey, ConfigurationItem> items = fetchConfigurationItems();

        configurationItemsAdapter.revalidateCache(items);
    }

    private Map<ConfigurationSearchKey, ConfigurationItem> fetchConfigurationItems() {
        ConfigurationSearchKey searchKey = new ConfigurationSearchKey(ConfigurationType.STREAM_TYPE_ENABLED, Provider.BETRADAR_V2.getId(), 1, -1, -1, false, 3, 3);
        ConfigurationItem configurationItem = new ConfigurationItem("true");

        return Map.of(searchKey, configurationItem);
    }

}
