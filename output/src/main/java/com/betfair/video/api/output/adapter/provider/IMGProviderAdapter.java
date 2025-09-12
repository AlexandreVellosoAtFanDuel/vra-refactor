package com.betfair.video.api.output.adapter.provider;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.valueobject.StreamDetails;
import com.betfair.video.api.domain.dto.valueobject.StreamParams;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.port.output.StreamingProviderPort;
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
