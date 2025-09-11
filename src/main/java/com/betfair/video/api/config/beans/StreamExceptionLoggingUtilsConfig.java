package com.betfair.video.api.config.beans;

import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamExceptionLoggingUtilsConfig {

    @Value("${videoapi.additional.info.enabled}")
    private boolean additionalInfoLoggingEnabled;

    @Bean
    public StreamExceptionLoggingUtils streamExceptionLoggingUtils() {
        return new StreamExceptionLoggingUtils(additionalInfoLoggingEnabled);
    }

}
