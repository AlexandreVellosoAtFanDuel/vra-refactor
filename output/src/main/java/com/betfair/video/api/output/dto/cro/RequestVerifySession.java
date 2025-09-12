package com.betfair.video.api.output.dto.cro;

public record RequestVerifySession(
        SessionToken sessionToken,
        String productEntityName,
        String performKeepAlive
) {
}
