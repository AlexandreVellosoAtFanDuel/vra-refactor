package com.betfair.video.api.infra.input.rest.dto.cro;

public record RequestVerifySession(
        SessionToken sessionToken,
        String productEntityName,
        String performKeepAlive
) {
}
