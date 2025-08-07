package com.betfair.video.api.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserGeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(UserGeolocationService.class);

    public void getUserGeolocation() {
        logger.info("Enter getUserGeolocation");
    }

}
