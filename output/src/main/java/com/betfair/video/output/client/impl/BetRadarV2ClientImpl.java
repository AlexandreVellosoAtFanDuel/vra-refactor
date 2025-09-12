package com.betfair.video.output.client.impl;

import com.betfair.video.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.domain.exception.StreamNotFoundException;
import com.betfair.video.output.client.BetRadarV2Client;
import com.betfair.video.output.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.output.dto.betradarv2.StreamUrlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class BetRadarV2ClientImpl implements BetRadarV2Client {

    @Value("${provider.betradar.v2.secret}")
    private String betradarV2Secret;

    private final RestClient betRadarV2Client;

    public BetRadarV2ClientImpl(RestClient betRadarV2Client) {
        this.betRadarV2Client = betRadarV2Client;
    }

    @Override
    public List<AudioVisualEventDto> getAudioVisualEvents(String streamStatus, String product) {
        var responseType = new ParameterizedTypeReference<List<AudioVisualEventDto>>() {
        };

        return betRadarV2Client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/events")
                        .queryParam("stream_status[]", streamStatus)
                        .queryParam("product[]", product)
                        .build()
                )
                .header("Authorization", "Bearer " + betradarV2Secret)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new StreamNotFoundException("Audio visual events not found", null);
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    throw new ErrorInDependentServiceException("BetRadar V2 service error", null);
                }))
                .body(responseType);
    }

    @Override
    public StreamUrlDto getStreamLink(String streamId, String streamFormat, String userIP) {
        return betRadarV2Client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/streams/{streamId}/{streamFormat}")
                        .build(streamId, streamFormat)
                )
                .header("Authorization", "Bearer " + betradarV2Secret)
                .header("X-Real-IP", userIP)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new StreamNotFoundException("Stream link not found", null);
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    throw new ErrorInDependentServiceException("BetRadar V2 service error", null);
                }))
                .body(StreamUrlDto.class);
    }

}
