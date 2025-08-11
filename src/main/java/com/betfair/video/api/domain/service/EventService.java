package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.VideoScheduleItem;
import com.betfair.video.api.domain.parser.ExternalIdParser;
import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private ExternalIdParser externalIdParser;

    public EventService(ExternalIdParser externalIdParser) {
        this.externalIdParser = externalIdParser;
    }

    public List<VideoScheduleItem> retrieveScheduleByExternalId(RequestContext context, User user, String externalIdSource, String externalId,
                                                                Integer channelTypeId, Integer mobileDeviceId) {

        if (!user.permissions().hasPermission(ServicePermission.VIDEO)) {
            logger.error("[{}]: Access permissions are insufficient for the requested operation or data for user (...)", context.uuid());
            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
        }

        ExternalIdSource source = ExternalIdSource.fromExternalIdSource(externalIdSource);

        ExternalId parsedExternalIds = externalIdParser.parseExternalIds(context, source, Set.of(externalId));
        if (parsedExternalIds.externalIds().isEmpty()) {
            logger.warn("[{}]: No external IDs were found", context.uuid());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        return Collections.emptyList();
    }

}
