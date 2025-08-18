package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;

import java.util.List;

public interface VideoStreamInfoPort {
    List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException;
}
