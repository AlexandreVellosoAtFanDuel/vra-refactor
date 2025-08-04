package com.betfair.video.api.infrastructure.in.interceptor;

import com.betfair.video.api.infrastructure.in.exception.ResponseCode;
import com.betfair.video.api.infrastructure.in.exception.VideoAPIException;
import com.betfair.video.api.infrastructure.in.exception.VideoAPIExceptionErrorCodeEnum;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlInterceptor.class);

    @Value("${videoapi.application.key}")
    private String applicationKey;

    private static final String X_UUID = "X-UUID";
    private static final String X_APPLICATION_KEY = "X-Application";
    private static final String X_IP = "X-IP";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uuid = request.getHeader(X_UUID);

        LOGGER.info("[{}]: Enter accessControlInterceptor", uuid);

        // Check that the application key is set
        if (!isAppKeyValid(request.getHeader(X_APPLICATION_KEY))) {
            LOGGER.error("[{}]: Valid application key is required for operation", uuid);

            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.UNKNOWN_CONSUMER, null);
        }

        if (!isUserIpAddressValid(request.getHeader(X_IP))) {
            LOGGER.error("[{}]: An IP address is required for operation", uuid);

            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.RESTRICTED_COUNTRY, null);
        }

        if (isStreamingOperation() && isAuthenticationIgnoredEmpty()) {
            if (!isSessionValid()) {
                LOGGER.error("[{}]: Valid session is required for operation", uuid);

                throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.NO_SESSION, null);
            }
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