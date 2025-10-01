package com.betfair.video.api.input.rest.interceptor;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.port.input.UserServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ContextInterceptor implements HandlerInterceptor {

    private static final String X_UUID = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String ACCOUNT_ID = "accountId";
    private static final String USER_ID = "userId";

    private final UserServicePort createUserUseCase;

    public ContextInterceptor(UserServicePort createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String uuid = request.getHeader(X_UUID);
        final String accountId = (String) request.getAttribute(ACCOUNT_ID);
        final String userId = (String) request.getAttribute(USER_ID);
        final String ip = request.getHeader(X_IP);

        User user = this.createUserUseCase.createUser(uuid, ip, accountId, userId);

        RequestContext context = new RequestContext(uuid, ip, user);

        request.setAttribute("context", context);

        return true;
    }
}
