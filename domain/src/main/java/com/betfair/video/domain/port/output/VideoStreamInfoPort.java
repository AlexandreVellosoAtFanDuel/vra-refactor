package com.betfair.video.domain.port.output;

import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.domain.exception.DataIsNotReadyException;

import java.util.List;

public interface VideoStreamInfoPort {
    List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException;
}
