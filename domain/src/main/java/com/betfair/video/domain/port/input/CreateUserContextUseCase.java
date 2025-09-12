package com.betfair.video.domain.port.input;

import com.betfair.video.domain.dto.entity.RequestContext;

import java.util.List;

public interface CreateUserContextUseCase {

    RequestContext createContext(String uuid, List<String> resolvedIps, String accountId, String userId);

}
