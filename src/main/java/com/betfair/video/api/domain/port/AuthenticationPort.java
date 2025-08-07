package com.betfair.video.api.domain.port;


public interface AuthenticationPort {

    void verifySession(String sessionToken);

}
