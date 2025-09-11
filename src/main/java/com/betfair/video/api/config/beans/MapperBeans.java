package com.betfair.video.api.config.beans;

import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperBeans {

    @Bean
    public VideoStreamInfoMapper videoStreamInfoMapper(GeoRestrictionsService geoRestrictionsService) {
        return new VideoStreamInfoMapper(geoRestrictionsService);
    }

}
