package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamDetailsParamEnum;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class BetradarV2ProviderAdapter implements StreamingProviderPort {

    @Override
    public boolean isEnabled() {
        // TODO: implement actual logic to determine if the provider is enabled
        return true;
    }

    @Override
    public StreamDetails getStreamDetails(ScheduleItem item, User user, StreamParams streamParams) {
        // TODO: implement actual logic to fetch stream details
        Map<StreamDetailsParamEnum, String> params = Map.of(
                StreamDetailsParamEnum.STREAM_FORMAT_PARAM_NAME, "hls"
        );

        return new StreamDetails(
                "https://api.livestreaming.imgarena.com/api/v2/streaming/events2/stream?operatorId=226",
                null,
                params
        );
    }

    @Override
    public Set<VideoQuality> getAvailableVideoQualityValues() {
        // TODO: implement actual logic to fetch available video quality values
        return Set.of(VideoQuality.HIGH);
    }
}
