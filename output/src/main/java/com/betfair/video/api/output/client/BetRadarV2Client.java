package com.betfair.video.api.output.client;

import com.betfair.video.api.output.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.output.dto.betradarv2.StreamUrlDto;

import java.util.List;

public interface BetRadarV2Client {

    List<AudioVisualEventDto> getAudioVisualEvents(
            String streamStatus,
            String product
    );

    StreamUrlDto getStreamLink(
            String streamId,
            String streamFormat,
            String userIP
    );

}
