package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoQuality;

import java.util.Set;

public interface StreamingProviderPort {

    boolean isEnabled();

    StreamDetails getStreamDetails(ScheduleItem item, User user, StreamParams streamParams);

    Set<VideoQuality> getAvailableVideoQualityValues();

}
