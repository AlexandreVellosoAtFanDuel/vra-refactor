package com.betfair.video.rest.interceptor;

import com.betfair.video.domain.exception.RestrictedCountryException;
import com.betfair.video.domain.exception.UnknownConsumerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AccessControlInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AccessControlInterceptor.class);

    @Value("${videoapi.application.key}")
    private String applicationKey;

    private static final String X_UUID = "X-UUID";
    private static final String X_APPLICATION_KEY = "X-Application";
    private static final String X_IP = "X-IP";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uuid = request.getHeader(X_UUID);

        logger.info("[{}]: Enter AccessControlInterceptor", uuid);

        // Check that the application key is set
        if (!isAppKeyValid(request.getHeader(X_APPLICATION_KEY))) {
            logger.error("[{}]: Valid application key is required for operation", uuid);

            throw new UnknownConsumerException("Valid application key is required for operation");
        }

        if (!isUserIpAddressValid(request.getHeader(X_IP))) {
            logger.error("[{}]: An IP address is required for operation", uuid);

            throw new RestrictedCountryException("An IP address is required for operation");
        }

        return true;
    }

    private boolean isAppKeyValid(String requestApplicationKey) {
        return Strings.isNotBlank(requestApplicationKey) && applicationKey.equalsIgnoreCase(requestApplicationKey);
    }

    // TODO: Validate user IP address
    private boolean isUserIpAddressValid(String ipAddress) {
        return Strings.isNotEmpty(ipAddress);
    }

} 