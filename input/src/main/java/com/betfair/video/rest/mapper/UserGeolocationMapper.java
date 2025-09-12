package com.betfair.video.rest.mapper;

import com.betfair.video.domain.dto.valueobject.Geolocation;
import com.betfair.video.rest.dto.UserGeolocationDto;

public class UserGeolocationMapper {

    private UserGeolocationMapper() {
    }

    public static UserGeolocationDto mapToDto(Geolocation geolocation) {
        return new UserGeolocationDto(geolocation.countryCode(), geolocation.subDivisionCode(), geolocation.dmaId());
    }

}
