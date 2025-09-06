package com.betfair.video.api.domain.entity;

import com.betfair.video.api.domain.valueobject.Geolocation;
import com.betfair.video.api.domain.valueobject.UserPermissions;

public record User(
        String accountId,
        String userId,
        String ip,
        Geolocation geolocation,
        UserPermissions permissions
) {

    public boolean isSuperUser() {
        // TODO: Implement actual logic
        return true;
    }

}
