package com.betfair.video.api.infra.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class BetradarV2RequestInterceptor implements RequestInterceptor {

    @Value("${provider.betradar.v2.secret}")
    private String betradarV2Secret;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + betradarV2Secret);
    }

}
