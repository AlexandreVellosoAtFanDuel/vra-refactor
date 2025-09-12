package com.betfair.video.output.dto;

public record ResponseVerifySession(
        String accountId,
        String userId,
        String hashedMainSessionId
) {
}
