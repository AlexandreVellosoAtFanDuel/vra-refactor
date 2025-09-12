package com.betfair.video.domain.service;

import com.betfair.video.domain.dto.valueobject.UserSessionDto;
import com.betfair.video.domain.port.input.VerifySessionUseCase;
import com.betfair.video.domain.port.output.AuthenticationPort;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements VerifySessionUseCase {

    private final AuthenticationPort authentication;

    public AuthenticationService(AuthenticationPort authentication) {
        this.authentication = authentication;
    }

    public UserSessionDto verifySession(String sessionToken) {
        return authentication.verifySession(sessionToken);
    }

}
