package com.betfair.video.api.infra.adapter.provider.betradarv2;

import com.betfair.video.api.infra.client.BetRadarV2Client;
import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamUrlDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BetRadarV2Adapter {

    private final BetRadarV2Client betRadarV2Client;

    public BetRadarV2Adapter(BetRadarV2Client betRadarV2Client) {
        this.betRadarV2Client = betRadarV2Client;
    }

    public List<AudioVisualEventDto> getAudioVisualEvents(BetradarActiveStreamsSearchKey key) {
        // TODO: Implement cache here
        return betRadarV2Client.getAudioVisualEvents(key.streamStatusIds(), key.streamProductIds());
    }

    public StreamUrlDto getStreamLink(String streamId, String streamType, String userIP) {
        return betRadarV2Client.getStreamLink(streamId, streamType, userIP);
    }
}
