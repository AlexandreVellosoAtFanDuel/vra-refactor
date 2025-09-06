package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.PermissionService;
import com.betfair.video.api.domain.service.UserGeolocationService;
import com.betfair.video.api.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig {

    @Bean
    public UserService userService(UserGeolocationService userGeolocationService, PermissionService permissionService) {
        return new UserService(userGeolocationService, permissionService);
    }

}
