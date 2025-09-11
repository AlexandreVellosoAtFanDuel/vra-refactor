package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.entity.User;

public interface CreateUserUseCase {

    User createUser(String uuid, String ip, String accountId, String userId);

}
