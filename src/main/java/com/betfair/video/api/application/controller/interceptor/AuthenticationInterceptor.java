package com.betfair.video.api.application.controller.interceptor;

import com.betfair.video.api.application.service.CROService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private static final String X_UUID = "X-UUID";
    private static final String X_AUTHENTICATION = "X-Authentication";

    private final CROService croService;

    public AuthenticationInterceptor(CROService croService) {
        this.croService = croService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uuid = request.getHeader(X_UUID);
        final String xAuthentication = request.getHeader(X_AUTHENTICATION);

        LOGGER.info("[{}]: Enter AuthenticationInterceptor", uuid);

        croService.verifySession(xAuthentication);

        return true;
    }

}
