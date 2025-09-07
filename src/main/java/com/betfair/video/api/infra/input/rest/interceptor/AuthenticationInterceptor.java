package com.betfair.video.api.infra.input.rest.interceptor;

import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;
import com.betfair.video.api.domain.port.input.VerifySessionUseCase;
import com.betfair.video.api.domain.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private static final String X_UUID = "X-UUID";
    private static final String X_AUTHENTICATION = "X-Authentication";

    private final VerifySessionUseCase verifySessionUseCase;

    public AuthenticationInterceptor(AuthenticationService verifySessionUseCase) {
        this.verifySessionUseCase = verifySessionUseCase;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uuid = request.getHeader(X_UUID);
        final String xAuthentication = request.getHeader(X_AUTHENTICATION);

        logger.info("[{}]: Enter AuthenticationInterceptor", uuid);

        UserSessionDto session = verifySessionUseCase.verifySession(xAuthentication);

        request.setAttribute("accountId", session.accountId());
        request.setAttribute("userId", session.userId());

        return true;
    }

}
