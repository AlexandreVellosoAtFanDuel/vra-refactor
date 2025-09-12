package com.betfair.video.domain.port.input;


import com.betfair.video.domain.dto.valueobject.Geolocation;

public interface UserGeolocationServicePort {

    Geolocation getUserGeolocation(String uuid, String ip);

}
