package com.betfair.video.api.config.beans;

import com.betfair.video.api.domain.dto.entity.ConfigurationItem;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.valueobject.DomainReferenceType;
import com.betfair.video.api.domain.dto.search.ConfigurationSearchKey;
import com.betfair.video.api.domain.dto.search.ReferenceTypeInfoByIdSearchKey;
import com.betfair.video.api.infra.output.dto.betradarv2.AudioVisualEventDto;
import com.hazelcast.collection.IList;
import com.hazelcast.config.Config;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringHazelcastConfiguration {

    @Bean
    public HazelcastInstance hazelcastInstance() { // (1)
        Config config = new Config();
        config.setClusterName("spring-hazelcast-cluster-from-java");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getInterfaces().addInterface("127.0.0.1");

        config.addMapConfig(buildConfigForBetRadarV2AudioVisualEvents());
        config.addMapConfig(buildConfigForConfigurationItems());
        config.addMapConfig(buildConfigForReferenceTypes());
        config.addListConfig(buildConfigForScheduleItems());

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IMap<String, List<AudioVisualEventDto>> betRadarV2AudioVisualEventsMap(HazelcastInstance instance) {
        return instance.getMap("betRadarV2AudioVisualEventsMap");
    }

    @Bean
    public IMap<ConfigurationSearchKey, ConfigurationItem> configurationItemsMap(HazelcastInstance instance) {
        return instance.getMap("configurationItemsMap");
    }

    @Bean
    public IMap<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> referenceTypesMap(HazelcastInstance instance) {
        return instance.getMap("referenceTypesMap");
    }

    @Bean
    public IList<ScheduleItem> scheduleItemCache(HazelcastInstance instance) {
        return instance.getList("scheduleItemCache");
    }

    private MapConfig buildConfigForBetRadarV2AudioVisualEvents() {
        return new MapConfig("betRadarV2AudioVisualEventsMap")
                .setTimeToLiveSeconds(60)
                .setMaxIdleSeconds(60)
                .setBackupCount(2);
    }

    private MapConfig buildConfigForConfigurationItems() {
        return new MapConfig("configurationItemsMap")
                .setTimeToLiveSeconds(300)
                .setMaxIdleSeconds(300)
                .setBackupCount(2);
    }

    private MapConfig buildConfigForReferenceTypes() {
        return new MapConfig("referenceTypesMap")
                .setTimeToLiveSeconds(600)
                .setMaxIdleSeconds(600)
                .setBackupCount(2);
    }

    private ListConfig buildConfigForScheduleItems() {
        return new ListConfig("scheduleItemCache")
                .setBackupCount(2);
    }

}