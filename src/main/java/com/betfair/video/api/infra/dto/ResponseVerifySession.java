package com.betfair.video.api.infra.dto;

public record ResponseVerifySession(
        String accountId,
        String userId,
        String hashedMainSessionId
) {
}
