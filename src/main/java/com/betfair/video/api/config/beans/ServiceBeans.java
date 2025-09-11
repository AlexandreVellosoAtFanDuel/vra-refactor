package com.betfair.video.api.config.beans;

import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.output.AuthenticationPort;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.output.GeolocationPort;
import com.betfair.video.api.domain.port.output.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.output.ProviderFactoryPort;
import com.betfair.video.api.domain.port.output.ReferenceTypePort;
import com.betfair.video.api.domain.port.output.VideoStreamInfoPort;
import com.betfair.video.api.domain.service.AtrScheduleService;
import com.betfair.video.api.domain.service.AuthenticationService;
import com.betfair.video.api.domain.service.BetsCheckService;
import com.betfair.video.api.domain.service.EventService;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import com.betfair.video.api.domain.service.PermissionService;
import com.betfair.video.api.domain.service.ScheduleItemService;
import com.betfair.video.api.domain.service.StreamService;
import com.betfair.video.api.domain.service.UserGeolocationService;
import com.betfair.video.api.domain.service.UserService;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeans {

    @Value("${streaming.brand.id}")
    private Integer streamingBrandId;

    @Value("${user.permissions.services}")
    private String userPermissionsServices;

    @Bean
    public AtrScheduleService atrScheduleService() {
        return new AtrScheduleService();
    }

    @Bean
    public BetsCheckService betsCheckService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        return new BetsCheckService(streamExceptionLoggingUtils);
    }

    @Bean
    public EventService eventService(StreamService streamService) {
        return new EventService(streamingBrandId, streamService);
    }

    @Bean
    public GeoRestrictionsService geoRestrictionsService() {
        return new GeoRestrictionsService();
    }

    @Bean
    public PermissionService permissionService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        return new PermissionService(userPermissionsServices, streamExceptionLoggingUtils);
    }

    @Bean
    public ScheduleItemService scheduleItemService(StreamExceptionLoggingUtils streamExceptionLoggingUtils,
                                                   VideoStreamInfoPort videoStreamInfoPort,
                                                   PermissionService permissionService,
                                                   ConfigurationItemsPort configurationItemsPort) {
        return new ScheduleItemService(streamExceptionLoggingUtils, videoStreamInfoPort, permissionService, configurationItemsPort);
    }

    @Bean
    public StreamService streamService(ConfigurationItemsPort configurationItemsPort,
                                       ScheduleItemService scheduleItemService, ProviderFactoryPort providerFactoryPort,
                                       PermissionService permissionService, BetsCheckService betsCheckService,
                                       DirectStreamConfigPort directStreamConfigPort, InlineStreamConfigPort inlineStreamConfigPort,
                                       ReferenceTypePort referenceTypePort, VideoStreamInfoMapper videoStreamInfoMapper) {
        return new StreamService(configurationItemsPort, scheduleItemService, providerFactoryPort,
                permissionService, betsCheckService, directStreamConfigPort, inlineStreamConfigPort,
                referenceTypePort, videoStreamInfoMapper);
    }

    @Bean
    public UserGeolocationService userGeolocationService(GeolocationPort geolocationPort) {
        return new UserGeolocationService(geolocationPort);
    }

    @Bean
    public UserService userService(UserGeolocationService userGeolocationService, PermissionService permissionService) {
        return new UserService(userGeolocationService, permissionService);
    }

    @Bean
    public AuthenticationService authenticationService(AuthenticationPort authenticationPort) {
        return new AuthenticationService(authenticationPort);
    }

}
