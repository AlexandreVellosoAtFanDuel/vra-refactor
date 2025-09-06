package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.dto.entity.Provider;
import com.betfair.video.api.domain.port.output.ProviderFactoryPort;
import com.betfair.video.api.domain.port.output.StreamingProviderPort;
import com.betfair.video.api.infra.output.adapter.provider.BetradarV2Adapter;
import com.betfair.video.api.infra.output.adapter.provider.IMGProviderAdapter;
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
    public StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId) {
        return providers.get(providerId);
    }

}
