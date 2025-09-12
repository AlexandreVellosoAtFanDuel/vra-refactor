package com.betfair.video.output.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${cro.api.base-url}")
    private String croBaseUrl;

    @Value("${provider.betradar.v2.url}")
    private String betradarV2BaseUrl;

    @Bean("croClient")
    public RestClient croClient() {
        return RestClient.builder()
                .baseUrl(croBaseUrl)
                .build();
    }

    @Bean("betRadarV2Client")
    public RestClient betRadarV2Client() {
        return RestClient.builder()
                .baseUrl(betradarV2BaseUrl)
                .build();
    }

}
