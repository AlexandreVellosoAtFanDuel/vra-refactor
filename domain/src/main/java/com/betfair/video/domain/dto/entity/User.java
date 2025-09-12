package com.betfair.video.domain.dto.entity;

import com.betfair.video.domain.dto.valueobject.Geolocation;
import com.betfair.video.domain.dto.valueobject.UserPermissions;

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
