package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.valueobject.Geolocation;
import com.betfair.video.api.domain.valueobject.UserPermissions;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserGeolocationService userGeolocationService;

    private final PermissionService permissionService;

    public UserService(UserGeolocationService userGeolocationService, PermissionService permissionService) {
        this.userGeolocationService = userGeolocationService;
        this.permissionService = permissionService;
    }

    public User createUserFromContext(RequestContext context) {
        Geolocation geolocation = userGeolocationService.getUserGeolocation(context);
        UserPermissions permissions = permissionService.createUserPermissions();

        return new User(
                context.accountId(),
                context.userId(),
                geolocation,
                permissions
        );
    }

}
