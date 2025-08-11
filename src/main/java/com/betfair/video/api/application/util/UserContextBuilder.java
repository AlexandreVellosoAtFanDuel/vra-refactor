package com.betfair.video.api.application.util;

import com.betfair.video.api.domain.entity.UserContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class UserContextBuilder {

    private static final String UUID_HEADER = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String X_IPS = "X-IPS";

    private UserContextBuilder() {
    }

    public static UserContext createUserFromRequest(HttpServletRequest request) {
        final String uuid = request.getHeader(UUID_HEADER);

        final String xIP = request.getHeader(X_IP);
        final String xIPs = request.getHeader(X_IPS);

        List<String> resolvedIps = new ArrayList<>();
        resolvedIps.add(xIP);
        resolvedIps.add(xIPs);

        return new UserContext(uuid, resolvedIps);
    }

}
