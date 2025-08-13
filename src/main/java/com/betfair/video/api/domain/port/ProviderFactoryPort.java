package com.betfair.video.api.domain.port;

public interface ProviderFactoryPort {

    StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId, Integer videoChannelId);

}
