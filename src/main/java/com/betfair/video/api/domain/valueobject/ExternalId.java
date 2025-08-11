package com.betfair.video.api.domain.valueobject;

import java.util.List;
import java.util.Map;

public record ExternalId(
        ExternalIdSource externalIdSource,
        Map<String, List<String>> externalIds
) {
}
