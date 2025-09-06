package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.port.output.GeolocationPort;
import com.betfair.video.api.domain.service.UserGeolocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserGeolocationServiceConfig {

    @Bean
    public UserGeolocationService userGeolocationService(GeolocationPort geolocationPort) {
        return new UserGeolocationService(geolocationPort);
    }

}
