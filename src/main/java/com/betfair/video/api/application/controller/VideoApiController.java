package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.service.UserGeolocationService;
import com.betfair.video.api.domain.valueobject.UserGeolocation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private static final String UUID_HEADER = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String X_IPS = "X-IPS";

    private final UserGeolocationService userGeolocationService;

    public VideoApiController(UserGeolocationService userGeolocationService) {
        this.userGeolocationService = userGeolocationService;
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        final String uuid = request.getHeader(UUID_HEADER);

        final String xIP = request.getHeader(X_IP);
        final String xIPs = request.getHeader(X_IPS);

        List<String> resolvedIps = new ArrayList<>();
        resolvedIps.add(xIP);
        resolvedIps.add(xIPs);

        logger.info("[{}] Enter retrieveUserGeolocation", uuid);

        User user = new User();
        user.setUuid(uuid);
        user.setIpAddresses(resolvedIps);

        UserGeolocation userGeolocation = userGeolocationService.getUserGeolocation(user);

        user.setCountryCode(userGeolocation.countryCode());
        user.setSubDivisionCode(userGeolocation.subDivisionCode());
        user.setDmaId(userGeolocation.dmaId());

        return new UserGeolocationDto(user.getCountryCode(), user.getSubDivisionCode(), user.getDmaId());
    }

}
