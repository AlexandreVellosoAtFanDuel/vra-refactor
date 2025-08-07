package com.betfair.video.api.application.dto.cro;

public record RequestVerifySession(
        SessionToken sessionToken,
        String productEntityName,
        String performKeepAlive
) {
}
