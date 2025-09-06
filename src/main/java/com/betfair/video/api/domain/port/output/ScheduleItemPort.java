package com.betfair.video.api.domain.port.output;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.search.VideoStreamInfoSearchKeyWrapper;

import java.util.List;

public interface ScheduleItemPort {

    ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper, User user);

    List<ScheduleItem> getAllAvailableEvents();

}
