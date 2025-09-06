package com.betfair.video.api.domain.port.output;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;

public interface GeolocationPort {

    Geolocation getUserGeolocation(RequestContext context);

}
