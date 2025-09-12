package com.betfair.video.domain.dto.entity;

public record RequestContext(
        String uuid,
        String ip,
        User user
) {
}
