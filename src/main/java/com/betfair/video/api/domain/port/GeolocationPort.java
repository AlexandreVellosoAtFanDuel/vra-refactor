package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.UserGeolocation;

public interface GeolocationPort {

    UserGeolocation getUserGeolocation(User user);

}
