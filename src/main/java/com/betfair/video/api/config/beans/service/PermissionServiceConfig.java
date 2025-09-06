package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.PermissionService;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionServiceConfig {

    @Value("${user.permissions.services}")
    private String userPermissionsServices;

    @Bean
    public PermissionService permissionService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        return new PermissionService(userPermissionsServices, streamExceptionLoggingUtils);
    }

}
