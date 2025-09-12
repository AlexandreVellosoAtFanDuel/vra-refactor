package com.betfair.video.domain.service;


import com.betfair.video.domain.dto.valueobject.Geolocation;
import com.betfair.video.domain.port.input.GetUserGeolocationUseCase;
import com.betfair.video.domain.port.output.GeolocationPort;
import org.springframework.stereotype.Service;

@Service
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
