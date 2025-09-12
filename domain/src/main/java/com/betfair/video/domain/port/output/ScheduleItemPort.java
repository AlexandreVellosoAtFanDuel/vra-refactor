package com.betfair.video.domain.port.output;

import com.betfair.video.domain.dto.entity.ScheduleItem;

import java.util.List;

public interface ScheduleItemPort {

    List<ScheduleItem> getAllAvailableEvents();

}
