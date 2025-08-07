package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoApiController.class);

    private static final String UUID_HEADER = "X-UUID";

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        LOGGER.info("[{}] Enter retrieveUserGeolocation", request.getHeader(UUID_HEADER));

        return new UserGeolocationDto("GB", "ENG", 12345);
    }

}
