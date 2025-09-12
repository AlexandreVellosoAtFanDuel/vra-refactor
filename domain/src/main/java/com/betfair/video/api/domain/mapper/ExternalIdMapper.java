package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.dto.valueobject.ExternalId;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExternalIdMapper {

    private static final String PRIMARY_SECONDARY_IDS_SEPARATOR = "@";

    private ExternalIdMapper() {
    }

    public static ExternalId map(ExternalIdSource externalIdSource, Set<String> externalIds) {
        Map<String, List<String>> ids = new HashMap<>();

        for (String externalId : externalIds) {
            if (StringUtils.isNotBlank(externalId)) {
                if (externalId.contains(PRIMARY_SECONDARY_IDS_SEPARATOR)) {
                    // Secondary IDs are ignored
                    ids.put(externalId.split(PRIMARY_SECONDARY_IDS_SEPARATOR)[0], Collections.emptyList());
                } else {
                    ids.put(externalId, Collections.emptyList());
                }
            }
        }

        return new ExternalId(externalIdSource, ids);
    }

}
