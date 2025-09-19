package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.valueobject.RetrieveScheduleByExternalIdParams;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;

public interface EventServicePort {

    VideoStreamInfo retrieveScheduleByExternalId(RetrieveScheduleByExternalIdParams params);

}
