package com.betfair.video.api.config.beans.mapper;

import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TypeStreamMapperConfig {

    @Bean
    public TypeStreamMapper typeStreamMapper() {
        return new TypeStreamMapper();
    }

}
