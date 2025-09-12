package com.betfair.video.domain.port.output;


import com.betfair.video.domain.dto.valueobject.Geolocation;

public interface GeolocationPort {

    Geolocation getUserGeolocation(String uuid, String ip);

}
