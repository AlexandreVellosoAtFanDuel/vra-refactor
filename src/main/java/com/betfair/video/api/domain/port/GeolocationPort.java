package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.UserContext;
import com.betfair.video.api.domain.valueobject.Geolocation;

public interface GeolocationPort {

    Geolocation getUserGeolocation(UserContext context);

}
