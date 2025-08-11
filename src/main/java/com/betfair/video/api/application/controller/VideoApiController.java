package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.application.util.UserUtil;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.service.UserGeolocationService;
import com.betfair.video.api.domain.valueobject.UserGeolocation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final UserGeolocationService userGeolocationService;

    public VideoApiController(UserGeolocationService userGeolocationService) {
        this.userGeolocationService = userGeolocationService;
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        User user = UserUtil.createUserFromRequest(request);

        logger.info("[{}] Enter retrieveUserGeolocation", user.getUuid());

        UserGeolocation userGeolocation = userGeolocationService.getUserGeolocation(user);

        return new UserGeolocationDto(userGeolocation.countryCode(), userGeolocation.subDivisionCode(), userGeolocation.dmaId());
    }

}
