package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;

import java.util.List;

public interface ScheduleItemPort {

    ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper, User user);

    List<ScheduleItem> getAllAvailableEvents();

}
