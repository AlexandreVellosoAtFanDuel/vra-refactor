package com.betfair.video.api.domain.entity;

import java.util.List;

public record RequestContext(
        String uuid,
        List<String> resolvedIps,
        String accountId,
        String userId
) {
}
