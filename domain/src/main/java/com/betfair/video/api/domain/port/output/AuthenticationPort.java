package com.betfair.video.api.domain.port.output;


import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;

public interface AuthenticationPort {

    UserSessionDto verifySession(String sessionToken);

}
