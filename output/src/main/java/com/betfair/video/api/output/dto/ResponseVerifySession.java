package com.betfair.video.api.output.dto;

public record ResponseVerifySession(
        String accountId,
        String userId,
        String hashedMainSessionId
) {
}
