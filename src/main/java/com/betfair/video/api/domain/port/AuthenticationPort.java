package com.betfair.video.api.domain.port;


import com.betfair.video.api.application.dto.cro.ResponseVerifySession;

public interface AuthenticationPort {

    ResponseVerifySession verifySession(String sessionToken);

}
