package com.betfair.video.api.config.beans.mapper;

import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalIdMapperConfig {

    @Bean
    public ExternalIdMapper externalIdMapper() {
        return new ExternalIdMapper();
    }

}
