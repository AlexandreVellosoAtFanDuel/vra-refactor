package com.betfair.video.api.domain.port.output;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;

import java.util.List;

public interface ScheduleItemPort {

    List<ScheduleItem> getAllAvailableEvents();

}
