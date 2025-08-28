package com.betfair.video.api.application.util;

import com.betfair.video.api.domain.entity.RequestContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class UserContextBuilder {

    private static final String UUID_HEADER = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String X_IPS = "X-IPS";
    private static final String ACCOUNT_ID = "accountId";
    private static final String USER_ID = "userId";

    private UserContextBuilder() {
    }

    public static RequestContext createContextFromRequest(HttpServletRequest request) {
        final String uuid = request.getHeader(UUID_HEADER);

        final String xIP = request.getHeader(X_IP);
        final String xIPs = request.getHeader(X_IPS);
        final String accountId = (String) request.getAttribute(ACCOUNT_ID);
        final String userId = (String) request.getAttribute(USER_ID);

        List<String> resolvedIps = new ArrayList<>();
        resolvedIps.add(xIP);
        resolvedIps.add(xIPs);

        return new RequestContext(uuid, resolvedIps, accountId, userId);
    }

}
