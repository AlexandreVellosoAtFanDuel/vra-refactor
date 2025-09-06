package com.betfair.video.api.infra.input.rest;

import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.infra.input.rest.dto.UserGeolocationDto;
import com.betfair.video.api.infra.input.rest.dto.VideoStreamInfoDto;
import com.betfair.video.api.infra.input.rest.mapper.UserGeolocationDtoMapper;
import com.betfair.video.api.infra.input.rest.mapper.VideoStreamInfoDtoMapper;
import com.betfair.video.api.infra.input.rest.util.UserContextBuilder;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.service.EventService;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/VideoAPI/v1.0")
public class VideoApiController {

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final UserContextBuilder userContextBuilder;

    private final EventService eventService;

    private final VideoStreamInfoDtoMapper videoStreamInfoDtoMapper;

    private final UserGeolocationDtoMapper userGeolocationDtoMapper;

    public VideoApiController(UserContextBuilder userContextBuilder, EventService eventService, VideoStreamInfoDtoMapper videoStreamInfoDtoMapper, UserGeolocationDtoMapper userGeolocationDtoMapper) {
        this.userContextBuilder = userContextBuilder;
        this.eventService = eventService;
        this.videoStreamInfoDtoMapper = videoStreamInfoDtoMapper;
        this.userGeolocationDtoMapper = userGeolocationDtoMapper;
    }

    @RequestMapping("/retrieveStreamInfoByExternalId")
    public VideoStreamInfoDto retrieveStreamInfoByExternalId(
            HttpServletRequest request,
            @RequestParam("externalIdSource") final String externalIdSource,
            @RequestParam("externalId") final String externalId,
            @RequestParam("channelTypeId") final Integer channelTypeId,
            @RequestParam(value = "channelSubTypeIds", required = false) final List<Integer> channelSubTypeIds,
            @RequestParam(value = "contentType", required = false) ContentTypeDto contentType,
            @RequestParam(value = "includeMetadata", required = false) Boolean includeMetadata,
            @RequestParam(value = "mobileDeviceId", required = false) final Integer mobileDeviceId,
            @RequestParam(value = "mobileOsVersion", required = false) String mobileOsVersion,
            @RequestParam(value = "mobileScreenDensityDpi", required = false) Integer mobileScreenDensityDpi,
            @RequestParam(value = "videoQuality", required = false) VideoQuality videoQuality,
            @RequestParam(value = "commentaryLanguage", required = false) String commentaryLanguage,
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @RequestParam(value = "providerParams", required = false) String providerParams
    ) {
        RequestContext context = userContextBuilder.createContextFromRequest(request);

        logger.info("[{}]: Enter retrieveStreamInfoByExternalId, source: {}, id: {}, channelTypeId: {}, mobileDeviceId: {}", context.uuid(), externalIdSource, externalId, channelTypeId, mobileDeviceId);

        logger.info("[{}]: User country sub-division: {}", context.uuid(), context.user().geolocation().subDivisionCode());

        VideoStreamInfo item = this.eventService.retrieveScheduleByExternalId(
                context,
                externalIdSource,
                externalId,
                channelTypeId,
                channelSubTypeIds,
                mobileDeviceId,
                mobileOsVersion,
                mobileScreenDensityDpi,
                videoQuality,
                commentaryLanguage,
                providerId,
                contentType,
                includeMetadata,
                providerParams
        );

        return videoStreamInfoDtoMapper.mapToDto(item);
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        RequestContext context = userContextBuilder.createContextFromRequest(request);

        logger.info("[{}] Enter retrieveUserGeolocation", context.uuid());

        return userGeolocationDtoMapper.mapToDto(context.user());
    }

}
