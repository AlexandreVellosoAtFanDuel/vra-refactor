package com.betfair.video.api.domain.valueobject;

import java.util.Set;

public record UserPermissions(
        Set<String> services
) {

    public boolean hasPermission(final ServicePermission servicePermission) {
        return services.contains(servicePermission.name());
    }

}
