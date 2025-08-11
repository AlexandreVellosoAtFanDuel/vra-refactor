package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.GeolocationPort;
import com.betfair.video.api.domain.valueobject.UserGeolocation;
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

    public UserGeolocation getUserGeolocation(User user) {
        logger.info("[{}] Enter getUserGeolocation", user.getUuid());

        return this.geolocationPort.getUserGeolocation(user);
    }

}
