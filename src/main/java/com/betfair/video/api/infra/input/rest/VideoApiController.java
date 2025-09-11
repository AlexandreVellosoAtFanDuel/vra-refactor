package com.betfair.video.api.infra.input.rest;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.port.input.CreateUserUseCase;
import com.betfair.video.api.domain.port.input.GetUserGeolocationUseCase;
import com.betfair.video.api.domain.port.input.RetrieveStreamInfoByExternalIdUseCase;
import com.betfair.video.api.domain.service.EventService;
import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.infra.input.rest.dto.UserGeolocationDto;
import com.betfair.video.api.infra.input.rest.dto.VideoQualityDto;
import com.betfair.video.api.infra.input.rest.dto.VideoStreamInfoDto;
import com.betfair.video.api.infra.input.rest.mapper.UserGeolocationMapper;
import com.betfair.video.api.infra.input.rest.mapper.VideoStreamInfoDtoMapper;
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

    private static final String UUID_HEADER = "X-UUID";
    private static final String X_IP = "X-IP";
    private static final String ACCOUNT_ID = "accountId";
    private static final String USER_ID = "userId";

    private static final Logger logger = LoggerFactory.getLogger(VideoApiController.class);

    private final RetrieveStreamInfoByExternalIdUseCase retrieveStreamInfoByExternalIdUseCase;

    private final VideoStreamInfoDtoMapper videoStreamInfoDtoMapper;

    private final CreateUserUseCase createUserUseCase;

    private final GetUserGeolocationUseCase getUserGeolocationUseCase;

    public VideoApiController(EventService retrieveStreamInfoByExternalIdUseCase, VideoStreamInfoDtoMapper videoStreamInfoDtoMapper, CreateUserUseCase createUserUseCase, GetUserGeolocationUseCase getUserGeolocationUseCase) {
        this.retrieveStreamInfoByExternalIdUseCase = retrieveStreamInfoByExternalIdUseCase;
        this.videoStreamInfoDtoMapper = videoStreamInfoDtoMapper;
        this.createUserUseCase = createUserUseCase;
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
        final String uuid = request.getHeader(UUID_HEADER);
        final String accountId = (String) request.getAttribute(ACCOUNT_ID);
        final String userId = (String) request.getAttribute(USER_ID);
        final String ip = request.getHeader(X_IP);

        User user = this.createUserUseCase.createUser(uuid, ip, accountId, userId);

        RequestContext context = new RequestContext(uuid, ip, user);

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

        return videoStreamInfoDtoMapper.mapToDto(item);
    }

    @RequestMapping("/retrieveUserGeolocation")
    public UserGeolocationDto retrieveUserGeolocation(HttpServletRequest request) {
        final String uuid = request.getHeader(UUID_HEADER);
        final String userIp = request.getHeader(X_IP);

        Geolocation userGeolocation = this.getUserGeolocationUseCase.getUserGeolocation(uuid, userIp);

        logger.info("[{}] Enter retrieveUserGeolocation", uuid);

        return UserGeolocationMapper.mapToDto(userGeolocation);
    }

}
