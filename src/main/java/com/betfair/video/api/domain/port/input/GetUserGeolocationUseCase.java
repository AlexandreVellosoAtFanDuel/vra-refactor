package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.valueobject.Geolocation;

public interface GetUserGeolocationUseCase {

    Geolocation getUserGeolocation(String uuid, String ip);

}
