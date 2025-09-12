package com.betfair.video.domain.service;


import com.betfair.video.domain.dto.entity.User;
import com.betfair.video.domain.dto.valueobject.Geolocation;
import com.betfair.video.domain.dto.valueobject.UserPermissions;
import com.betfair.video.domain.port.input.UserServicePort;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServicePort {

    private final UserGeolocationService userGeolocationService;

    private final PermissionService permissionService;

    public UserService(UserGeolocationService userGeolocationService, PermissionService permissionService) {
        this.userGeolocationService = userGeolocationService;
        this.permissionService = permissionService;
    }

    @Override
    public User createUser(String uuid, String ip, String accountId, String userId) {
        Geolocation geolocation = userGeolocationService.getUserGeolocation(uuid, ip);
        UserPermissions permissions = permissionService.createUserPermissions();

        return new User(
                accountId,
                userId,
                ip,
                geolocation,
                permissions
        );
    }
}
