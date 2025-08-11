package com.betfair.video.api.application.util;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.service.UserGeolocationService;
import com.betfair.video.api.domain.valueobject.UserGeolocation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserContextBuilder {

    private static final String UUID_HEADER = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String X_IPS = "X-IPS";

    private final UserGeolocationService userGeolocationService;

    public UserContextBuilder(UserGeolocationService userGeolocationService) {
        this.userGeolocationService = userGeolocationService;
    }

    public User createUserFromRequest(HttpServletRequest request) {
        final String uuid = request.getHeader(UUID_HEADER);

        final String xIP = request.getHeader(X_IP);
        final String xIPs = request.getHeader(X_IPS);

        List<String> resolvedIps = new ArrayList<>();
        resolvedIps.add(xIP);
        resolvedIps.add(xIPs);

        User user = new User();
        user.setUuid(uuid);
        user.setIpAddresses(resolvedIps);

        UserGeolocation geolocation = userGeolocationService.getUserGeolocation(user);

        user.setCountryCode(geolocation.countryCode());
        user.setSubDivisionCode(geolocation.subDivisionCode());
        user.setDmaId(geolocation.dmaId());

        return user;
    }

}
