package com.betfair.video.api.domain.port;


import com.betfair.video.api.infra.output.dto.ResponseVerifySession;

public interface AuthenticationPort {

    ResponseVerifySession verifySession(String sessionToken);

}
