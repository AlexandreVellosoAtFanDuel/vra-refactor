package com.betfair.video.domain.port.output;

public interface ProviderFactoryPort {

    StreamingProviderPort getStreamingProviderByIdAndVideoChannelId(Integer providerId);

}
