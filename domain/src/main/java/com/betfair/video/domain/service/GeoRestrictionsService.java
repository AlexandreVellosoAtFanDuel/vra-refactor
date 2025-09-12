package com.betfair.video.domain.service;

import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.port.input.GeoRestrictionsUseCase;
import org.springframework.stereotype.Service;

@Service
public class GeoRestrictionsService implements GeoRestrictionsUseCase {

    public String getProviderBlockedCountries(ScheduleItem scheduleItem) {
        // TODO: Fetch provider blocked countries
        return "ZA";
    }

}
