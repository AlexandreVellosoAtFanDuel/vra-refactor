package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.springframework.stereotype.Service;

@Service
public class ScheduleItemService {

    public VideoStreamState getVideoStreamStateBasedOnScheduleItem(ScheduleItem item, User user) {
        return null;
    }

    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper searchKey, User user) {
        return null;
    }

}
