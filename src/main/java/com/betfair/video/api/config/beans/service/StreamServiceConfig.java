package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.output.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.output.ProviderFactoryPort;
import com.betfair.video.api.domain.port.output.ReferenceTypePort;
import com.betfair.video.api.domain.service.BetsCheckService;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import com.betfair.video.api.domain.service.PermissionService;
import com.betfair.video.api.domain.service.ScheduleItemService;
import com.betfair.video.api.domain.service.StreamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamServiceConfig {

    @Bean
    public StreamService streamService(ConfigurationItemsPort configurationItemsPort,
                                       ScheduleItemService scheduleItemService, ProviderFactoryPort providerFactoryPort,
                                       PermissionService permissionService, BetsCheckService betsCheckService,
                                       DirectStreamConfigPort directStreamConfigPort, InlineStreamConfigPort inlineStreamConfigPort,
                                       GeoRestrictionsService geoRestrictionsService, VideoStreamInfoMapper videoStreamInfoMapper,
                                       ReferenceTypePort referenceTypePort) {
        return new StreamService(configurationItemsPort, scheduleItemService, providerFactoryPort,
                permissionService, betsCheckService, directStreamConfigPort, inlineStreamConfigPort,
                geoRestrictionsService, videoStreamInfoMapper, referenceTypePort);
    }

}
