package com.betfair.video.api.application.controller;

import com.betfair.video.api.application.dto.ContentTypeDto;
import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.application.dto.VideoScheduleItemDto;
import com.betfair.video.api.application.mapper.VideoScheduleItemDtoMapper;
import com.betfair.video.api.application.util.UserContextBuilder;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.VideoScheduleItem;
import com.betfair.video.api.domain.service.EventService;
import com.betfair.video.api.domain.service.UserService;
import com.betfair.video.api.domain.valueobject.VideoQuality;
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

    private final UserService userService;

    private final EventService eventService;

    private final VideoScheduleItemDtoMapper videoScheduleItemDtoMapper;

    public VideoApiController(UserService userService, EventService eventService, VideoScheduleItemDtoMapper videoScheduleItemDtoMapper) {
        this.userService = userService;
        this.eventService = eventService;
        this.videoScheduleItemDtoMapper = videoScheduleItemDtoMapper;
    }

    @RequestMapping("/retrieveStreamInfoByExternalId")
    public List<VideoScheduleItemDto> retrieveStreamInfoByExternalId(
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
        RequestContext context = UserContextBuilder.createContextFromRequest(request);

        logger.info("[{}]: Enter retrieveStreamInfoByExternalId, source: {}, id: {}, channelTypeId: {}, mobileDeviceId: {}", context.uuid(), externalIdSource, externalId, channelTypeId, mobileDeviceId);

        User user = userService.createUserFromContext(context);

        logger.info("[{}]: User country sub-division: {}", context.uuid(), user.geolocation().subDivisionCode());

        List<VideoScheduleItem> items = this.eventService.retrieveScheduleByExternalId(
                context,
                user,
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

        return videoScheduleItemDtoMapper.mapToDtoList(items);
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        RequestContext context = UserContextBuilder.createContextFromRequest(request);
        User user = userService.createUserFromContext(context);

        logger.info("[{}] Enter retrieveUserGeolocation", context.uuid());

        return new UserGeolocationDto(user.geolocation().countryCode(), user.geolocation().subDivisionCode(), user.geolocation().dmaId());
    }

}
