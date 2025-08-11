package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.valueobject.ServicePermission;
import com.betfair.video.api.domain.valueobject.UserPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Value("${user.permissions.services}")
    private String userPermissionsServices;

    public UserPermissions createUserPermissions() {
        Set<String> services = getUserServices();

        return new UserPermissions(services);
    }

    private Set<String> getUserServices() {
        Set<String> services = new HashSet<>();

        if ("ALL".equalsIgnoreCase(userPermissionsServices)) {
            services.addAll(Arrays.stream(ServicePermission.values()).toList()
                    .stream()
                    .map(Enum::name)
                    .toList());
        } else {
            services.addAll(Arrays.stream(userPermissionsServices.split(",")).toList()
                    .stream()
                    .map(s -> ServicePermission.valueOf(s.trim()).name())
                    .collect(Collectors.toSet()));
        }

        return services;
    }

}
