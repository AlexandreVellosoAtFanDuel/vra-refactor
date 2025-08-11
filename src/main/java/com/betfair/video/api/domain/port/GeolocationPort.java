package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.valueobject.Geolocation;

public interface GeolocationPort {

    Geolocation getUserGeolocation(RequestContext context);

}
