package com.betfair.video.api.infra.output.dto;

public record ResponseVerifySession(
        String accountId,
        String userId,
        String hashedMainSessionId
) {
}
