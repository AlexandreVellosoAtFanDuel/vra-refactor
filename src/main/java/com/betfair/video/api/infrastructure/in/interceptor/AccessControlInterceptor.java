package com.betfair.video.api.infrastructure.in.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AccessControlInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlInterceptor.class);

    private static final String X_UUID = "X-UUID";
    private static final String X_APPLICATION_KEY = "X-Application";
    private static final String X_IP = "X-IP";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uuid = request.getHeader(X_UUID);

        LOGGER.info("[{}]: Enter accessControlInterceptor", uuid);

        final String applicationKey = request.getHeader(X_APPLICATION_KEY);

        // Check that the application key is set
        if (!isAppKeyValid(applicationKey)) {
            LOGGER.error("[{}]: Valid application key is required for operation", uuid);
            return false;
        }

        final String ipAddress = request.getHeader(X_IP);

        if (!isUserIpAddressValid(ipAddress)) {
            LOGGER.error("[{}]: An IP address is required for operation", uuid);
            return false;
        }

        if (isStreamingOperation() && isAuthenticationIgnoredEmpty()) {
            if (!isSessionValid()) {
                LOGGER.error("[{}]: Valid session is required for operation", uuid);
                return false;
            }
        }

        return true;
    }

    // TODO: Validate it with the properties, and so on
    private boolean isAppKeyValid(String applicationKey) {
        return Strings.isNotEmpty(applicationKey);
    }

    // TODO: Validate user IP address
    private boolean isUserIpAddressValid(String ipAddress) {
        return Strings.isNotEmpty(ipAddress);
    }

    // TODO: Implement this
    private boolean isStreamingOperation() {
        return false;
    }

    // TODO: Implement this
    private boolean isAuthenticationIgnoredEmpty() {
        return false;
    }

    // TODO: Implement this
    private boolean isSessionValid() {
        return true;
    }

} 