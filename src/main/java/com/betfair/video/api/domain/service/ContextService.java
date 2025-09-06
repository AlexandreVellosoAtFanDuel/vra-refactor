package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.User;

import java.util.List;

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

