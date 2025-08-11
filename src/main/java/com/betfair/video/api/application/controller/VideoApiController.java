package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.application.util.UserContextBuilder;
import com.betfair.video.api.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final UserContextBuilder userContextBuilder;

    public VideoApiController(UserContextBuilder userContextBuilder) {
        this.userContextBuilder = userContextBuilder;
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        User user = userContextBuilder.createUserFromRequest(request);

        logger.info("[{}] Enter retrieveUserGeolocation", user.getUuid());

        return new UserGeolocationDto(user.getCountryCode(), user.getSubDivisionCode(), user.getDmaId());
    }

}
