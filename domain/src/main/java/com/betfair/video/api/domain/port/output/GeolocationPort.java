package com.betfair.video.api.domain.port.output;


import com.betfair.video.api.domain.dto.valueobject.Geolocation;

public interface GeolocationPort {

    Geolocation getUserGeolocation(String uuid, String ip);

}
