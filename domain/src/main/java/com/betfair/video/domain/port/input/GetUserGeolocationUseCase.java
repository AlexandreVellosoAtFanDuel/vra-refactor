package com.betfair.video.domain.port.input;


import com.betfair.video.domain.dto.valueobject.Geolocation;

public interface GetUserGeolocationUseCase {

    Geolocation getUserGeolocation(String uuid, String ip);

}
