package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import com.betfair.video.api.domain.service.EventService;
import com.betfair.video.api.domain.service.StreamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventServiceConfig {

    @Value("${streaming.brand.id}")
    private Integer streamingBrandId;

    @Bean
    public EventService eventService(ExternalIdMapper externalIdMapper, TypeStreamMapper typeStreamMapper, StreamService streamService) {
        return new EventService(streamingBrandId, externalIdMapper, typeStreamMapper, streamService);
    }

}
