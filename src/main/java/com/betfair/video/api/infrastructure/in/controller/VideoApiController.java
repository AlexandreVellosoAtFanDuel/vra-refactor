package com.betfair.video.api.infrastructure.in.controller;

import com.betfair.video.api.infrastructure.in.dto.UserGeolocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoApiController.class);

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(@RequestHeader("X-UUID") String uuid,
                                                      @RequestHeader("X-IP") String ip,
                                                      @RequestHeader("X-Sportsbook-Region") String sportsbookRegion,
                                                      @RequestHeader("X-Authentication") String authentication) {
        LOGGER.info("[{}]: Enter retrieveUserGeolocation", "some-unique-request-id");

        return new UserGeolocationDto("GB", "ENG", 12345);
    }

}
