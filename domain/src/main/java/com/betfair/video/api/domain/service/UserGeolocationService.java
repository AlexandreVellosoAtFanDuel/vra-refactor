package com.betfair.video.api.domain.service;


import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.port.input.UserGeolocationServicePort;
import com.betfair.video.api.domain.port.output.GeolocationPort;
import org.springframework.stereotype.Service;

@Service
public class UserGeolocationService implements UserGeolocationServicePort {

    private final GeolocationPort geolocationPort;

    public UserGeolocationService(GeolocationPort geolocationPort) {
        this.geolocationPort = geolocationPort;
    }

    @Override
    public Geolocation getUserGeolocation(String uuid, String ip) {
        return geolocationPort.getUserGeolocation(uuid, ip);
    }

}
