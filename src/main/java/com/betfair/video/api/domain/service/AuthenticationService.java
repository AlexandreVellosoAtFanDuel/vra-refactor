package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;
import com.betfair.video.api.domain.port.input.VerifySessionUseCase;
import com.betfair.video.api.domain.port.output.AuthenticationPort;

public class AuthenticationService implements VerifySessionUseCase {

    private final AuthenticationPort authentication;

    public AuthenticationService(AuthenticationPort authentication) {
        this.authentication = authentication;
    }

    public UserSessionDto verifySession(String sessionToken) {
        return authentication.verifySession(sessionToken);
    }

}
