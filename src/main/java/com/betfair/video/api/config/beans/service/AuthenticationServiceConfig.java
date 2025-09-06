package com.betfair.video.api.config.beans.service;

import com.betfair.video.api.domain.port.output.AuthenticationPort;
import com.betfair.video.api.domain.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationServiceConfig {

    @Bean
    public AuthenticationService authenticationService(AuthenticationPort authentication) {
        return new AuthenticationService(authentication);
    }

}
