package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ScheduleItemPort;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.springframework.stereotype.Component;

@Component
public class ScheduleItemAdapter implements ScheduleItemPort {

    @Override
    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper, User user) {
        return null;
    }

}
