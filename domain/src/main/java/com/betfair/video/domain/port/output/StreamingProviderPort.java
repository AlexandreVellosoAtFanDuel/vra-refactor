package com.betfair.video.domain.port.output;

import com.betfair.video.domain.dto.entity.RequestContext;
import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.dto.valueobject.StreamDetails;
import com.betfair.video.domain.dto.valueobject.StreamParams;
import com.betfair.video.domain.dto.valueobject.VideoQuality;

import java.util.Set;

public interface StreamingProviderPort {

    boolean isEnabled();

    StreamDetails getStreamDetails(RequestContext context, ScheduleItem item, StreamParams streamParams);

    Set<VideoQuality> getAvailableVideoQualityValues();

}
