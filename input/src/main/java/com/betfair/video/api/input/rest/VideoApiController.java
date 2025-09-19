package com.betfair.video.api.input.rest;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.port.input.EventServicePort;
import com.betfair.video.api.domain.port.input.UserGeolocationServicePort;
import com.betfair.video.api.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.input.rest.dto.UserGeolocationDto;
import com.betfair.video.api.input.rest.dto.VideoQualityDto;
import com.betfair.video.api.input.rest.dto.VideoStreamInfoDto;
import com.betfair.video.api.input.rest.mapper.UserGeolocationMapper;
import com.betfair.video.api.input.rest.mapper.VideoStreamInfoMapper;
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

    private final EventServicePort retrieveStreamInfoByExternalIdUseCase;

    private final UserGeolocationServicePort getUserGeolocationUseCase;

    public VideoApiController(EventServicePort retrieveStreamInfoByExternalIdUseCase, UserGeolocationServicePort getUserGeolocationUseCase) {
        this.retrieveStreamInfoByExternalIdUseCase = retrieveStreamInfoByExternalIdUseCase;
        this.getUserGeolocationUseCase = getUserGeolocationUseCase;
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
            @RequestParam(value = "videoQuality", required = false) VideoQualityDto videoQuality,
            @RequestParam(value = "commentaryLanguage", required = false) String commentaryLanguage,
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @RequestParam(value = "providerParams", required = false) String providerParams
    ) {
        RequestContext context = (RequestContext) request.getAttribute("context");

        logger.info("[{}]: Enter retrieveStreamInfoByExternalId, source: {}, id: {}, channelTypeId: {}, mobileDeviceId: {}", context.uuid(), externalIdSource, externalId, channelTypeId, mobileDeviceId);

        logger.info("[{}]: User country sub-division: {}", context.uuid(), context.user().geolocation().subDivisionCode());

        VideoStreamInfo item = this.retrieveStreamInfoByExternalIdUseCase.retrieveScheduleByExternalId(
                context,
                externalIdSource,
                externalId,
                channelTypeId,
                channelSubTypeIds,
                mobileDeviceId,
                mobileOsVersion,
                mobileScreenDensityDpi,
                videoQuality != null ? VideoQuality.fromValue(videoQuality.getValue()) : null,
                commentaryLanguage,
                providerId,
                contentType != null ? ContentType.fromValue(contentType.getValue()) : null,
                includeMetadata,
                providerParams
        );

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
