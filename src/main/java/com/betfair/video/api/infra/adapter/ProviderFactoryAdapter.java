package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.infra.adapter.provider.BetradarV2ProviderAdapter;
import org.springframework.stereotype.Component;

@Component
public class ProviderFactoryAdapter implements ProviderFactoryPort {

    private BetradarV2ProviderAdapter betradarV2ProviderAdapter;

    @Override
    public StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId, Integer videoChannelId) {
        return betradarV2ProviderAdapter;
    }

}
