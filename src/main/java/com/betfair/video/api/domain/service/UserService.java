package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.dto.valueobject.UserPermissions;

public class UserService {

    private final UserGeolocationService userGeolocationService;

    private final PermissionService permissionService;

    public UserService(UserGeolocationService userGeolocationService, PermissionService permissionService) {
        this.userGeolocationService = userGeolocationService;
        this.permissionService = permissionService;
    }

    public User createUserFromContext(RequestContext context, String accountId, String userId) {
        Geolocation geolocation = userGeolocationService.getUserGeolocation(context);
        UserPermissions permissions = permissionService.createUserPermissions();

        return new User(
                accountId,
                userId,
                context.resolvedIps().getFirst(),
                geolocation,
                permissions
        );
    }

}
