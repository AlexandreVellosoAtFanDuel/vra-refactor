package com.betfair.video.api.domain.dto.entity;

public record RequestContext(
        String uuid,
        String ip,
        User user
) {
}
