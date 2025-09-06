package com.betfair.video.api.domain.service;

import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
import com.betfair.video.api.domain.port.output.AuthenticationPort;
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
