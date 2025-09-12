package com.betfair.video.output.dto.cro;

public record RequestVerifySession(
        SessionToken sessionToken,
        String productEntityName,
        String performKeepAlive
) {
}
