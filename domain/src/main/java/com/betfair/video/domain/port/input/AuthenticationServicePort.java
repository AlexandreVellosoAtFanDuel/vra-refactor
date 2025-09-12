package com.betfair.video.domain.port.input;

import com.betfair.video.domain.dto.valueobject.UserSessionDto;

public interface AuthenticationServicePort {

    UserSessionDto verifySession(String sessionToken);

}
