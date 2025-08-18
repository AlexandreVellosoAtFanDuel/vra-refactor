package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BetradarV2ProviderAdapter implements StreamingProviderPort {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public StreamDetails getStreamDetails(ScheduleItem item, User user, StreamParams streamParams) {
        return null;
    }

    @Override
    public Set<VideoQuality> getAvailableVideoQualityValues() {
        return null;
    }
}
