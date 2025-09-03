package com.betfair.video.api.infra.config;

import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class SpringHazelcastConfiguration {

    @Bean
    public HazelcastInstance hazelcastInstance() { // (1)
        Config config = new Config();
        config.setClusterName("spring-hazelcast-cluster-from-java");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getInterfaces().addInterface("127.0.0.1");
        
        MapConfig mapConfig = new MapConfig("betRadarV2AudioVisualEventsMap")
                .setTimeToLiveSeconds(30)
                .setBackupCount(2);
        config.addMapConfig(mapConfig);
        
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IMap<String, List<AudioVisualEventDto>> betRadarV2AudioVisualEventsMap(HazelcastInstance instance) {
        return instance.getMap("betRadarV2AudioVisualEventsMap");
    }

}