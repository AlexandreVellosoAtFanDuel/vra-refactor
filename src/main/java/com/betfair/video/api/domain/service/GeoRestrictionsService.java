package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import org.springframework.stereotype.Service;

@Service
public class GeoRestrictionsService {
    public String getProviderBlockedCountries(ScheduleItem scheduleItem) {
        // TODO: Fetch provider blocked countries
        return "ZA";
    }
}
