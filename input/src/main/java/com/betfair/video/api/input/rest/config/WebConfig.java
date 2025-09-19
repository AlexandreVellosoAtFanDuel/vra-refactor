package com.betfair.video.api.input.rest.config;

import com.betfair.video.api.input.rest.interceptor.AccessControlInterceptor;
import com.betfair.video.api.input.rest.interceptor.AuthenticationInterceptor;
import com.betfair.video.api.input.rest.interceptor.ContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    private final AccessControlInterceptor accessControlInterceptor;

    private final ContextInterceptor contextInterceptor;

    public WebConfig(@Lazy AuthenticationInterceptor authenticationInterceptor, AccessControlInterceptor accessControlInterceptor, ContextInterceptor contextInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.accessControlInterceptor = accessControlInterceptor;
        this.contextInterceptor = contextInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(accessControlInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info");

        registry
                .addInterceptor(authenticationInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info")
                .excludePathPatterns("/VideoAPI/v1.0/retrieveUserGeolocation");

        registry
                .addInterceptor(contextInterceptor)
                .addPathPatterns("/VideoAPI/**")
                .excludePathPatterns("/actuator/**", "/health", "/info");
    }
} 