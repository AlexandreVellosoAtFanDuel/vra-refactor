package com.betfair.video.api.domain.port.output;

public interface ProviderFactoryPort {

    StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId);

}
