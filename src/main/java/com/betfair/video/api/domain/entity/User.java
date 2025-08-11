package com.betfair.video.api.domain.entity;

import com.betfair.video.api.domain.valueobject.Geolocation;
import com.betfair.video.api.domain.valueobject.UserPermissions;

public record User(
        Geolocation geolocation,
        UserPermissions permissions
) {
}
