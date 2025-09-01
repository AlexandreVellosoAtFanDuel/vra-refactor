package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContextService {

    private final UserService userService;

    public ContextService(UserService userService) {
        this.userService = userService;
    }

    public RequestContext createContext(String uuid, List<String> resolvedIps, String accountId, String userId) {
        RequestContext context = new RequestContext(uuid, resolvedIps);
        User user = userService.createUserFromContext(context, accountId, userId);

        context.setUser(user);

        return context;
    }

}

