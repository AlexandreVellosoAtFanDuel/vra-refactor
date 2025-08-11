package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.valueobject.Geolocation;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserGeolocationService userGeolocationService;

    public UserService(UserGeolocationService userGeolocationService) {
        this.userGeolocationService = userGeolocationService;
    }

    public User createUserFromContext(RequestContext context) {
        Geolocation geolocation = userGeolocationService.getUserGeolocation(context);

        return new User(
                null,
                context.uuid(),
                context.resolvedIps(),
                geolocation.countryCode(),
                geolocation.subDivisionCode(),
                geolocation.dmaId()
        );
    }

}
