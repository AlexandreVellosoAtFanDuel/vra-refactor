package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.application.util.UserContextBuilder;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.UserContext;
import com.betfair.video.api.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final UserService userService;

    public VideoApiController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        UserContext context = UserContextBuilder.createUserFromRequest(request);
        User user = userService.createUserFromContext(context);

        logger.info("[{}] Enter retrieveUserGeolocation", user.uuid());

        return new UserGeolocationDto(user.countryCode(), user.subDivisionCode(), user.dmaId());
    }

}
