package com.betfair.video.domain.port.output;


import com.betfair.video.domain.dto.valueobject.UserSessionDto;

public interface AuthenticationPort {

    UserSessionDto verifySession(String sessionToken);

}
