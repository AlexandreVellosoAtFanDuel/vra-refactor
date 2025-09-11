package com.betfair.video.api.infra.input.rest.mapper;

import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.infra.input.rest.dto.UserGeolocationDto;

public class UserGeolocationMapper {

    private UserGeolocationMapper() {
    }

    public static UserGeolocationDto mapToDto(Geolocation geolocation) {
        return new UserGeolocationDto(geolocation.countryCode(), geolocation.subDivisionCode(), geolocation.dmaId());
    }

}
