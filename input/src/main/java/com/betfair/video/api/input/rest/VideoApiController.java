package com.betfair.video.api.input.rest;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.dto.valueobject.RetrieveScheduleByExternalIdParams;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.port.input.EventServicePort;
import com.betfair.video.api.domain.port.input.UserGeolocationServicePort;
import com.betfair.video.api.input.rest.dto.RetrieveStreamInfoByExternalIdDto;
import com.betfair.video.api.input.rest.dto.UserGeolocationDto;
import com.betfair.video.api.input.rest.dto.VideoStreamInfoDto;
import com.betfair.video.api.input.rest.mapper.RetrieveStreamInfoByExternalIdMapper;
import com.betfair.video.api.input.rest.mapper.UserGeolocationMapper;
import com.betfair.video.api.input.rest.mapper.VideoStreamInfoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final EventServicePort retrieveStreamInfoByExternalIdUseCase;

    private final UserGeolocationServicePort getUserGeolocationUseCase;

    public VideoApiController(EventServicePort retrieveStreamInfoByExternalIdUseCase, UserGeolocationServicePort getUserGeolocationUseCase) {
        this.retrieveStreamInfoByExternalIdUseCase = retrieveStreamInfoByExternalIdUseCase;
        this.getUserGeolocationUseCase = getUserGeolocationUseCase;
    }

    @RequestMapping("/retrieveStreamInfoByExternalId")
    public VideoStreamInfoDto retrieveStreamInfoByExternalId(HttpServletRequest request, RetrieveStreamInfoByExternalIdDto params) {
        RequestContext context = (RequestContext) request.getAttribute("context");

        logger.info("[{}]: Enter retrieveStreamInfoByExternalId, source: {}, id: {}, channelTypeId: {}, mobileDeviceId: {}",
                context.uuid(), params.externalIdSource(), params.externalId(), params.channelTypeId(), params.mobileDeviceId());

        logger.info("[{}]: User country sub-division: {}", context.uuid(), context.user().geolocation().subDivisionCode());

        RetrieveScheduleByExternalIdParams mappedParams = RetrieveStreamInfoByExternalIdMapper.map(context, params);

        VideoStreamInfo item = this.retrieveStreamInfoByExternalIdUseCase.retrieveScheduleByExternalId(mappedParams);

        return VideoStreamInfoMapper.mapToDto(item);
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        RequestContext context = (RequestContext) request.getAttribute("context");

        Geolocation userGeolocation = this.getUserGeolocationUseCase.getUserGeolocation(context.uuid(), context.ip());

        logger.info("[{}] Enter retrieveUserGeolocation", context.uuid());

        return UserGeolocationMapper.mapToDto(userGeolocation);
    }

}
