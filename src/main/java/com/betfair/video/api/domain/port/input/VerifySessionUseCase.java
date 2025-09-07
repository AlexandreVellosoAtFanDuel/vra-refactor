package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;

public interface VerifySessionUseCase {

    UserSessionDto verifySession(String sessionToken);

}
