package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.entity.RequestContext;

import java.util.List;

public interface CreateUserContextUseCase {

    RequestContext createContext(String uuid, List<String> resolvedIps, String accountId, String userId);

}
