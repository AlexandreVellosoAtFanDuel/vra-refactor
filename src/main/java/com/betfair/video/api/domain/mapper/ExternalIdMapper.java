package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ExternalIdMapper {

    private static final Logger logger = LoggerFactory.getLogger(ExternalIdMapper.class);

    private static final String PRIMARY_SECONDARY_IDS_SEPARATOR = "@";

    private static final Set<ExternalIdSource> validExternalIdSources = Set.of(
            ExternalIdSource.BETFAIR_EVENT,
            ExternalIdSource.BETFAIR_MARKET,
            ExternalIdSource.TIMEFORM,
            ExternalIdSource.EXCHANGE_RACE,
            ExternalIdSource.RAMP
    );

    public ExternalId map(RequestContext context, ExternalIdSource externalIdSource, Set<String> externalIds) {

        if (!validExternalIdSources.contains(externalIdSource)) {
            logger.error("[{}]: No external ID parser was found for external ID provider {}", context.uuid(), externalIdSource);
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

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
