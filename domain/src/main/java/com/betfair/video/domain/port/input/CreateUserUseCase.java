package com.betfair.video.domain.port.input;

import com.betfair.video.domain.dto.entity.User;

public interface CreateUserUseCase {

    User createUser(String uuid, String ip, String accountId, String userId);

}
