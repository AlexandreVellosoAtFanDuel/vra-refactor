package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;

public interface GeoRestrictionsUseCase {

    String getProviderBlockedCountries(ScheduleItem scheduleItem);

}
