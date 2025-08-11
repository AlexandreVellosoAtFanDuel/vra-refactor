package com.betfair.video.api.domain.entity;

import java.util.List;

public record UserContext(
        String uuid,
        List<String> resolvedIps
) {
}
