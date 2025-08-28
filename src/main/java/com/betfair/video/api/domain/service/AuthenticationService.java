package com.betfair.video.api.domain.service;

import com.betfair.video.api.infra.dto.ResponseVerifySession;
import com.betfair.video.api.domain.port.AuthenticationPort;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationPort authentication;

    public AuthenticationService(AuthenticationPort authentication) {
        this.authentication = authentication;
    }

    public ResponseVerifySession verifySession(String sessionToken) {
        return authentication.verifySession(sessionToken);
    }

}
