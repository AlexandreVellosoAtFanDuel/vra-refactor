package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.infra.adapter.provider.BetradarV2Adapter;
import com.betfair.video.api.infra.adapter.provider.IMGProviderAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProviderFactoryAdapter implements ProviderFactoryPort {

    private final Map<Integer, StreamingProviderPort> providers = new HashMap<>();

    public ProviderFactoryAdapter(IMGProviderAdapter imgProviderAdapter, BetradarV2Adapter betradarV2ProviderAdapter) {
        providers.put(Provider.IMG.getId(), imgProviderAdapter);
        providers.put(Provider.BETRADAR_V2.getId(), betradarV2ProviderAdapter);
    }

    @Override
    public StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId, Integer videoChannelId) {
        StreamingProviderPort provider = providers.get(Provider.BETRADAR_V2.getId());

        if (provider == null) {
            throw new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, null);
        }

        return provider;
    }

}
