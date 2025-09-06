package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.GeoRestrictionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoRestrictionsServiceConfig {

    @Bean
    public GeoRestrictionsService geoRestrictionsService() {
        return new GeoRestrictionsService();
    }

}
