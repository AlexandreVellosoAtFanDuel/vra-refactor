package com.betfair.video.api.infra.config;

import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.hazelcast.collection.IList;
import com.hazelcast.config.Config;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SpringHazelcastConfiguration {

    @Bean
    public HazelcastInstance hazelcastInstance() { // (1)
        Config config = new Config();
        config.setClusterName("spring-hazelcast-cluster-from-java");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getInterfaces().addInterface("127.0.0.1");

        config.addListConfig(new ListConfig("betRadarV2AudioVisualEventsList").setBackupCount(2));
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IList<AudioVisualEventDto> betRadarV2AudioVisualEventsList(HazelcastInstance instance) {
        return instance.getList("betRadarV2AudioVisualEventsList");
    }

}