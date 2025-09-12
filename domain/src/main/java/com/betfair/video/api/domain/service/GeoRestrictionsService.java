package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.port.input.GeoRestrictionsServicePort;
import org.springframework.stereotype.Service;

@Service
public class GeoRestrictionsService implements GeoRestrictionsServicePort {

    public String getProviderBlockedCountries(ScheduleItem scheduleItem) {
        // TODO: Fetch adapter.provider blocked countries
        return "ZA";
    }

}
