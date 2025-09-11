package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.port.input.GetUserGeolocationUseCase;
import com.betfair.video.api.domain.port.output.GeolocationPort;


public class UserGeolocationService implements GetUserGeolocationUseCase {

    private final GeolocationPort geolocationPort;

    public UserGeolocationService(GeolocationPort geolocationPort) {
        this.geolocationPort = geolocationPort;
    }

    @Override
    public Geolocation getUserGeolocation(String uuid, String ip) {
        return geolocationPort.getUserGeolocation(uuid, ip);
    }

}
