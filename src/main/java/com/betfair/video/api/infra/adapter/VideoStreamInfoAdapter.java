package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.port.VideoStreamInfoPort;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoStreamInfoAdapter implements VideoStreamInfoPort {

    @Override
    public List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException {
        return null;
    }

}
