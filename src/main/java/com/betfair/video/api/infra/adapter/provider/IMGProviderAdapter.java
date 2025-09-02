package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class IMGProviderAdapter implements StreamingProviderPort {

    @Value("${provider.img.enabled}")
    private String isEnabled;

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(isEnabled);
    }

    @Override
    public StreamDetails getStreamDetails(RequestContext context, ScheduleItem item, StreamParams streamParams) {
        return null;
    }

    @Override
    public Set<VideoQuality> getAvailableVideoQualityValues() {
        return Collections.emptySet();
    }
}
