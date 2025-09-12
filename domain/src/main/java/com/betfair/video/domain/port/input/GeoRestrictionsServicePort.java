package com.betfair.video.domain.port.input;

import com.betfair.video.domain.dto.entity.ScheduleItem;

public interface GeoRestrictionsServicePort {

    String getProviderBlockedCountries(ScheduleItem scheduleItem);

}
