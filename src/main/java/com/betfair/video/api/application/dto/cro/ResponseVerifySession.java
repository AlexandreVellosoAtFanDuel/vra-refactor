package com.betfair.video.api.application.dto.cro;

public record ResponseVerifySession(
        String accountId,
        String userId,
        String hashedMainSessionId
) {
}
