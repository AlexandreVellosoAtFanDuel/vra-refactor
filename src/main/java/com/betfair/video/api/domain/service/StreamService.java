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
import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
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

    private final ConfigurationItemsPort configurationItemsPort;

    private final AtrScheduleService atrScheduleService;

    private final ScheduleItemService scheduleItemService;

    private final ProviderFactoryPort providerFactoryPort;

    private final PermissionService permissionService;

    private final BetsCheckService betsCheckService;

    public StreamService(ReferenceTypesPort referenceTypesPort, ConfigurationItemsPort configurationItemsPort, AtrScheduleService atrScheduleService, ScheduleItemService scheduleItemService, ProviderFactoryPort providerFactoryPort, PermissionService permissionService, BetsCheckService betsCheckService) {
        this.referenceTypesPort = referenceTypesPort;
        this.configurationItemsPort = configurationItemsPort;
        this.atrScheduleService = atrScheduleService;
        this.scheduleItemService = scheduleItemService;
        this.providerFactoryPort = providerFactoryPort;
        this.permissionService = permissionService;
        this.betsCheckService = betsCheckService;
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

        ScheduleItem item = scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, user);

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
        VideoStreamInfo streamInfo = checkAndProcess(searchKey, identifier, searchKey.getExternalIdSource(), item, context, user, isArchivedVideo, includeMetadata);

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

    private VideoStreamInfo checkAndProcess(VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier identifier, ExternalIdSource externalIdSource, ScheduleItem item, RequestContext context, User user, boolean isArchivedStream, boolean includeMetadata) {

        StreamingProviderPort provider = providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(item.providerId(), item.videoChannelType());

        if (provider == null) {
            logger.error("[{}]: No provider was found for the given provider ID ({}).",
                    context.uuid(), item.providerId());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        if (!provider.isEnabled()) {
            logger.error("[{}]: Provider with ID {} is not enabled.", context.uuid(), item.providerId());
            throw new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, null);
        }

        boolean userHasPermissionAgainstItem = permissionService.checkUserPermissionsAgainstItem(item, user);

        if (!userHasPermissionAgainstItem) {
            logger.warn("[{}]: User {} does not have permission to access stream for item with videoItemId {}. User country: {},{}",
                    context.uuid(), user.accountId(), item.videoItemId(), user.geolocation().countryCode(), user.geolocation().subDivisionCode());
            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
        }

        // Make sure that the video is currently showing before proceed
        checkStreamStatus(item, externalIdSource, user, isArchivedStream);

        BetsCheckerStatusEnum bbvStatus = scheduleItemService.isItemWatchAndBetSupported(item)
                ? BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG
                : betsCheckService.getBBVStatus(identifier, item, user, isArchivedStream);

        // Validate BBV status, if it is not valid an exception is thrown
        betsCheckService.validateBBVStatus(bbvStatus, item, user, context);

        return getStreamInfoForItem(item, provider, searchKey, user, isArchivedStream, includeMetadata);
    }

    private void checkStreamStatus(ScheduleItem item, ExternalIdSource externalIdSource, User user, boolean isArchivedStream) throws VideoAPIException {
        if (!isArchivedStream) {
            VideoStreamState streamState;
            if (Provider.ATR_PROVIDERS.contains(item.providerId())) {
                streamState = atrScheduleService.getCachedStreamState(item, user);
            } else {
                streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item, user);
            }

            if (!ExternalIdSource.BETFAIR_VIDEO.equals(externalIdSource) &&
                    (TypeStream.PRE_VID.equals(item.streamTypeId()) && VideoStreamState.FINISHED.equals(streamState))) {
                //in case of finished paddock we should return NOT_STARTED because in means that we are currently inside a window
                //between end of paddock and before start of event stream
                streamState = VideoStreamState.NOT_STARTED;
            }

            scheduleItemService.checkIsCurrentlyShowingAndThrow(streamState, item.videoItemId(), user, item.betfairSportsType());
        }
    }

    private VideoStreamInfo getStreamInfoForItem(ScheduleItem item, StreamingProviderPort provider, VideoStreamInfoByExternalIdSearchKey searchKey, User user, boolean isArchivedStream, boolean includeMetadata) {
        return new VideoStreamInfo();
    }

}
