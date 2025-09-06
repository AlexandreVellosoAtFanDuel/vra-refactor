package com.betfair.video.api.config.beans.mapper;

import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoStreamInfoMapperConfig {

    @Bean
    public VideoStreamInfoMapper videoStreamInfoMapper() {
        return new VideoStreamInfoMapper();
    }

}
