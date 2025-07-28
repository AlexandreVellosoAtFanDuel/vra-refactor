package com.betfair.video.api.infrastructure.in.config;

import com.betfair.video.api.infrastructure.in.interceptor.AccessControlInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final AccessControlInterceptor authenticationInterceptor;
    
    public WebConfig(AccessControlInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info");
    }
} 