package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ScheduleItemPort;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.springframework.stereotype.Service;

@Service
public class ScheduleItemService {

    private final ScheduleItemPort scheduleItemPort;

    public ScheduleItemService(ScheduleItemPort scheduleItemPort) {
        this.scheduleItemPort = scheduleItemPort;
    }

    public VideoStreamState getVideoStreamStateBasedOnScheduleItem(ScheduleItem item, User user) {
        return null;
    }

    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper searchKey, User user) {
        return null;
    }

    public void checkIsCurrentlyShowingAndThrow(VideoStreamState streamState, Long aLong, User user, Integer integer) {
    }

    public boolean isItemWatchAndBetSupported(ScheduleItem item) {
        return false;
    }
}
