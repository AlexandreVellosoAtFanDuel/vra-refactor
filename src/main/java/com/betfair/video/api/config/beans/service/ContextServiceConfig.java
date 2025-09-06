package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.ContextService;
import com.betfair.video.api.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextServiceConfig {

    @Bean
    public ContextService contextService(UserService userService) {
        return new ContextService(userService);
    }

}
