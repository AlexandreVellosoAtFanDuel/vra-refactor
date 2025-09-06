package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.VideoStreamInfoPort;
import com.betfair.video.api.domain.service.PermissionService;
import com.betfair.video.api.domain.service.ScheduleItemService;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleItemServiceConfig {

    @Bean
    public ScheduleItemService scheduleItemService(StreamExceptionLoggingUtils streamExceptionLoggingUtils,
                                                   VideoStreamInfoPort videoStreamInfoPort,
                                                   PermissionService permissionService,
                                                   ConfigurationItemsPort configurationItemsPort) {
        return new ScheduleItemService(streamExceptionLoggingUtils, videoStreamInfoPort, permissionService, configurationItemsPort);
    }

}
