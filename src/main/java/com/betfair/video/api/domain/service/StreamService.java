package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.port.ScheduleItemPort;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class StreamService {

    private static final Logger logger = LoggerFactory.getLogger(StreamService.class);

    private final ReferenceTypesPort referenceTypesPort;

    private final ScheduleItemPort scheduleItemPort;

    private final ConfigurationItemsPort configurationItemsPort;

    private final AtrScheduleService atrScheduleService;

    private final ScheduleItemService scheduleItemService;

    public StreamService(ReferenceTypesPort referenceTypesPort, ScheduleItemPort scheduleItemPort, ConfigurationItemsPort configurationItemsPort, AtrScheduleService atrScheduleService, ScheduleItemService scheduleItemService) {
        this.referenceTypesPort = referenceTypesPort;
        this.scheduleItemPort = scheduleItemPort;
        this.configurationItemsPort = configurationItemsPort;
        this.atrScheduleService = atrScheduleService;
        this.scheduleItemService = scheduleItemService;
    }

    public VideoStreamInfo getStreamInfoByExternalId(final VideoStreamInfoByExternalIdSearchKey searchKey, final RequestContext context, final User user, final boolean includeMetadata) {

        if (searchKey.getProviderId() != null && referenceTypesPort.findReferenceTypeById(searchKey.getProviderId(), ReferenceTypeId.VIDEO_PROVIDER) == null) {
            logger.error("[{}]: No provider was found for the given provider ID ({}). User country: {},{}.",
                    context.uuid(), searchKey.getProviderId(), user.geolocation().countryCode(), user.geolocation().subDivisionCode());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        boolean isArchivedVideo = ExternalIdSource.TIMEFORM.equals(searchKey.getExternalIdSource());

        VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper = new VideoStreamInfoSearchKeyWrapper(
                searchKey.getExternalIdSource(),
                searchKey,
                null,
                null
        );

        ScheduleItem item = scheduleItemPort.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, user);

        //try to find leading stream if current is not live yet
        ScheduleItem paddockItem = tryFindLeadingStream(item, searchKey.getPrimaryId(), videoStreamInfoSearchKeyWrapper, context, user, isArchivedVideo);
        if (paddockItem != null) {
            logger.info("[{}]: Found leading stream for external id {}. Video id: {}. User country: {},{}",
                    context.uuid(), searchKey.getPrimaryId(), paddockItem.videoItemId(), user.geolocation().countryCode(), user.geolocation().subDivisionCode());

            item = paddockItem;
        }

        populateCurrentRequestContextParams(user, item, searchKey.getExternalIdSource().getExternalIdDescription(),
                searchKey.getPrimaryId(), String.valueOf(item.videoItemId()));

        VideoRequestIdentifier identifier = videoStreamInfoSearchKeyWrapper.getVideoRequestIdentifier(item);

        // A single video item was found
        VideoStreamInfo streamInfo = checkAndProcess(searchKey, identifier, searchKey.getExternalIdSource(), item, user, isArchivedVideo, includeMetadata);

        if (isArchivedVideo && streamInfo != null) {
            streamInfo.setTimeformRaceId(searchKey.getPrimaryId());
        }

        return streamInfo;
    }

    private ScheduleItem tryFindLeadingStream(ScheduleItem item, String externalId, VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper,
                                              RequestContext context, User user, boolean isArchivedVideo) throws VideoAPIException {

        boolean isStreamTypeAllowed = configurationItemsPort.isStreamTypeAllowed(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), TypeStream.PRE_VID, item.brandId());

        //if racing live stream is not live yet - try to find paddock stream
        if (!isArchivedVideo && isStreamTypeAllowed) {

            VideoStreamState streamState;
            if (Provider.ATR_PROVIDERS.contains(item.providerId())) {
                streamState = atrScheduleService.getCachedStreamState(item, user);
            } else {
                streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item, user);
            }

            if (VideoStreamState.NOT_STARTED.equals(streamState)) {
                logger.info("[{}]: Stream by externalId {} is not live yet. Trying to find pre-event paddock stream. User country: {},{}",
                        context.uuid(), externalId, user.geolocation().countryCode(), user.geolocation().subDivisionCode());

                videoStreamInfoSearchKeyWrapper.videoStreamInfoByExternalIdSearchKey()
                        .setStreamTypeIds(Collections.singleton(TypeStream.PRE_VID.getId()));

                videoStreamInfoSearchKeyWrapper.videoStreamInfoByExternalIdSearchKey().setProviderId(item.providerId());

                try {
                    return scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, user);
                } catch (VideoAPIException e) {
                    //if paddock stream doesn't exist or any other error - return null
                    logger.info("[{}]: Got {} error while trying to find paddock stream for externalId {}. Returning null. Parent stream will be processed further. User country: {},{}",
                            context.uuid(), e.getErrorCode(), externalId, user.geolocation().countryCode(), user.geolocation().subDivisionCode());

                    return null;
                }
            }
        }

        return null;
    }

    private void populateCurrentRequestContextParams(User user, ScheduleItem item, String requestedStreamIdSource,
                                                     String requestedStreamId, String videoItemId) {
    }

    private VideoStreamInfo checkAndProcess(VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier identifier, ExternalIdSource externalIdSource, ScheduleItem item, User user, boolean isArchivedVideo, boolean includeMetadata) {
        return new VideoStreamInfo();
    }
}
