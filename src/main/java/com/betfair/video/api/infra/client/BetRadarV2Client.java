package com.betfair.video.api.infra.client;

import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamUrlDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "betradar-v2-client", url = "${provider.betradar.v2.url}")
public interface BetRadarV2Client {

    @GetMapping("/v1/events")
    List<AudioVisualEventDto> getAudioVisualEvents(
            @RequestParam("stream_status[]") String streamStatus,
            @RequestParam("product[]") String product
    );

    @GetMapping("/v1/streams/{streamId}/{streamFormat}")
    StreamUrlDto getStreamLink(
            @PathVariable String streamId,
            @PathVariable String streamFormat,
            @RequestHeader("X-Real-IP") String userIP
    );

}
