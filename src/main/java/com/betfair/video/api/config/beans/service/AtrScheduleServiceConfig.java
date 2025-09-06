package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.service.AtrScheduleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtrScheduleServiceConfig {

    @Bean
    public AtrScheduleService atrScheduleService(){
        return new AtrScheduleService();
    }

}
