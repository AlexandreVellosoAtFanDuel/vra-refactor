package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.BetsCheckService;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BetsCheckServiceConfig {

    @Bean
    public BetsCheckService betsCheckService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        return new BetsCheckService(streamExceptionLoggingUtils);
    }

}
