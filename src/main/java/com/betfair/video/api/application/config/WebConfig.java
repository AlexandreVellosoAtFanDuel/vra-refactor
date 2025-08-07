package com.betfair.video.api.application.config;

import com.betfair.video.api.application.controller.interceptor.AccessControlInterceptor;
import com.betfair.video.api.application.controller.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    private final AccessControlInterceptor accessControlInterceptor;

    public WebConfig(@Lazy AuthenticationInterceptor authenticationInterceptor, AccessControlInterceptor accessControlInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.accessControlInterceptor = accessControlInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authenticationInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info");

        registry
                .addInterceptor(accessControlInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info");
    }
} 