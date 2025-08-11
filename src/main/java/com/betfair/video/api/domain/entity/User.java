package com.betfair.video.api.domain.entity;

import java.util.List;

public record User(
        String id,
        String uuid,
        List<String> ipAddresses,
        String countryCode,
        String subDivisionCode,
        Integer dmaId
) {
}
