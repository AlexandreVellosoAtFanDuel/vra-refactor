package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.port.input.GeoRestrictionsUseCase;

public class GeoRestrictionsService implements GeoRestrictionsUseCase {

    public String getProviderBlockedCountries(ScheduleItem scheduleItem) {
        // TODO: Fetch provider blocked countries
        return "ZA";
    }

}
