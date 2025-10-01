package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.port.input.GeoRestrictionsServicePort;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeoRestrictionsService implements GeoRestrictionsServicePort {

    private static final Logger logger = LoggerFactory.getLogger(GeoRestrictionsService.class);

    private final ConfigurationItemsPort configurationItemsPort;

    public GeoRestrictionsService(ConfigurationItemsPort configurationItemsPort) {
        this.configurationItemsPort = configurationItemsPort;
    }

    public String getProviderBlockedCountries(ScheduleItem scheduleItem) {
        if (scheduleItem == null) {
            logger.info("Can't find provider geo restrictions for an empty video item");
            return null;
        }

        return configurationItemsPort.findProviderBlockedCountries(scheduleItem.providerId(), scheduleItem.videoChannelType(),
                scheduleItem.betfairSportsType(), scheduleItem.streamTypeId(), scheduleItem.brandId());
    }

}
