package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.UserContext;
import com.betfair.video.api.domain.port.GeolocationPort;
import com.betfair.video.api.domain.valueobject.Geolocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserGeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(UserGeolocationService.class);

    private final GeolocationPort geolocationPort;

    public UserGeolocationService(GeolocationPort geolocationPort) {
        this.geolocationPort = geolocationPort;
    }

    public Geolocation getUserGeolocation(UserContext context) {
        logger.info("[{}] Enter getUserGeolocation", context.uuid());

        return this.geolocationPort.getUserGeolocation(context);
    }

}
