package com.betfair.video.api.domain.port.output;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;

import java.util.List;

public interface VideoStreamInfoPort {
    List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException;
}
