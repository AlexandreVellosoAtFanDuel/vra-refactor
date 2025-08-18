package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.ContentType;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamDetailsParamEnum;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


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

    private final ArchivedStreamService archivedStreamService;

    private final DirectStreamConfigPort directStreamConfigPort;

    private final InlineStreamConfigPort inlineStreamConfigPort;

    private final GeoRestrictionsService geoRestrictionsService;

    public StreamService(ReferenceTypesPort referenceTypesPort, ConfigurationItemsPort configurationItemsPort,
                         AtrScheduleService atrScheduleService, ScheduleItemService scheduleItemService,
                         ProviderFactoryPort providerFactoryPort, PermissionService permissionService,
                         BetsCheckService betsCheckService, ArchivedStreamService archivedStreamService,
                         DirectStreamConfigPort directStreamConfigPort, InlineStreamConfigPort inlineStreamConfigPort,
                         GeoRestrictionsService geoRestrictionsService) {
        this.referenceTypesPort = referenceTypesPort;
        this.configurationItemsPort = configurationItemsPort;
        this.atrScheduleService = atrScheduleService;
        this.scheduleItemService = scheduleItemService;
        this.providerFactoryPort = providerFactoryPort;
        this.permissionService = permissionService;
        this.betsCheckService = betsCheckService;
        this.archivedStreamService = archivedStreamService;
        this.directStreamConfigPort = directStreamConfigPort;
        this.inlineStreamConfigPort = inlineStreamConfigPort;
        this.geoRestrictionsService = geoRestrictionsService;
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

        ScheduleItem item = scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, context, user);

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
        validateScheduleItem(searchKey, identifier, searchKey.getExternalIdSource(), item, context, user, isArchivedVideo);

        StreamingProviderPort provider = providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(item.providerId(), item.videoChannelType());

        VideoStreamInfo streamInfo = getStreamInfoForItem(item, provider, searchKey, user, isArchivedVideo, includeMetadata);

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
                streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item);
            }

            if (VideoStreamState.NOT_STARTED.equals(streamState)) {
                logger.info("[{}]: Stream by externalId {} is not live yet. Trying to find pre-event paddock stream. User country: {},{}",
                        context.uuid(), externalId, user.geolocation().countryCode(), user.geolocation().subDivisionCode());

                videoStreamInfoSearchKeyWrapper.getVideoStreamInfoByExternalIdSearchKey()
                        .setStreamTypeIds(Collections.singleton(TypeStream.PRE_VID.getId()));

                videoStreamInfoSearchKeyWrapper.getVideoStreamInfoByExternalIdSearchKey().setProviderId(item.providerId());

                try {
                    return scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, context, user);
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

    private void validateScheduleItem(VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier identifier, ExternalIdSource externalIdSource, ScheduleItem item, RequestContext context, User user, boolean isArchivedVideo) {

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
        checkStreamStatus(item, externalIdSource, context, user, isArchivedVideo);

        BetsCheckerStatusEnum bbvStatus = scheduleItemService.isItemWatchAndBetSupported(item)
                ? BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG
                : betsCheckService.getBBVStatus(identifier, item, user, isArchivedVideo);

        // Validate BBV status, if it is not valid an exception is thrown
        betsCheckService.validateBBVStatus(bbvStatus, item, user, context);
    }

    private void checkStreamStatus(ScheduleItem item, ExternalIdSource externalIdSource, RequestContext context, User user, boolean isArchivedStream) throws VideoAPIException {
        if (!isArchivedStream) {
            VideoStreamState streamState;
            if (Provider.ATR_PROVIDERS.contains(item.providerId())) {
                streamState = atrScheduleService.getCachedStreamState(item, user);
            } else {
                streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item);
            }

            if (!ExternalIdSource.BETFAIR_VIDEO.equals(externalIdSource) &&
                    (TypeStream.PRE_VID.equals(item.streamTypeId()) && VideoStreamState.FINISHED.equals(streamState))) {
                //in case of finished paddock we should return NOT_STARTED because in means that we are currently inside a window
                //between end of paddock and before start of event stream
                streamState = VideoStreamState.NOT_STARTED;
            }

            scheduleItemService.checkIsCurrentlyShowingAndThrow(streamState, item.videoItemId(), context, user, item.betfairSportsType());
        }
    }

    private VideoStreamInfo getStreamInfoForItem(ScheduleItem item, StreamingProviderPort provider, VRAStreamSearchKey searchKey,
                                                 User user, boolean isArchivedStream, boolean includeMetadata) throws VideoAPIException {

        StreamParams params = new StreamParams(
                searchKey.getCommentaryLanguage(),
                searchKey.getVideoQuality(),
                searchKey.getMobileDeviceId(),
                searchKey.getMobileOsVersion(),
                searchKey.getMobileScreenDensityDpi(),
                searchKey.getProviderParams(),
                false,
                searchKey.getChannelTypeId()
        );

        StreamDetails streamDetails;
        if (isArchivedStream) {
            streamDetails = archivedStreamService.getStreamDetails(item, user, params);
        } else {
            streamDetails = provider.getStreamDetails(item, user, params);
        }

        boolean isDirectStream;
        if (isArchivedStream) {
            isDirectStream = directStreamConfigPort.isArchivedProviderInList(item.providerId(), searchKey.getMobileDeviceId() != null);
        } else {
            isDirectStream = directStreamConfigPort.isProviderInList(item.providerId(), item.videoChannelType());
        }
        boolean isInlineStream;
        if (isArchivedStream) {
            isInlineStream = isDirectStream && inlineStreamConfigPort.isArchivedProviderInList(item.providerId(), searchKey.getMobileDeviceId() != null);
        } else {
            isInlineStream = isDirectStream && inlineStreamConfigPort.isProviderInList(item.providerId(), item.videoChannelType());
        }

        VideoStreamInfoMapper generator = new VideoStreamInfoMapper(item, streamDetails, geoRestrictionsService, includeMetadata);
        generator.setUser(user);
        generator.setAvailableVideoQualityValues(provider.getAvailableVideoQualityValues());
        generator.setDirectStream(isDirectStream);
        generator.setInlineStream(isInlineStream);
        generator.setContentType(convertStreamTypeIdToContentType(item.streamTypeId()));
        generator.setSizeRestrictions(configurationItemsPort.getSizeRestrictions(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId()));
        generator.setVideoPlayerConfig(getVideoPlayerConfig(item, streamDetails));
        generator.setSportReferenceType(referenceTypesPort.findReferenceTypeById(item.betfairSportsType(), ReferenceTypeId.SPORTS_TYPE));

        setDefaultParameterValues(generator, item);

        return generator.generateResponse();
    }

    private String getVideoPlayerConfig(ScheduleItem scheduleItem, StreamDetails streamDetails) {
        String videoPlayerConfig = null;
        ConfigurationItem configurationItem = configurationItemsPort.findVideoPlayerConfig(scheduleItem.providerId(), scheduleItem.videoChannelType(),
                scheduleItem.betfairSportsType(), scheduleItem.streamTypeId(), scheduleItem.brandId());

        boolean configurationItemExist = configurationItem != null;
        if (configurationItemExist) {
            String streamFormat = streamDetails.params().get(StreamDetailsParamEnum.STREAM_FORMAT_PARAM_NAME);
            boolean streamFormatExist = streamFormat != null && !"".equals(streamFormat);

            String configValue = configurationItem.value();
            boolean configValueExist = configValue != null && !"".equals(configValue);

            if (streamFormatExist) {
                if (configValueExist) {
                    videoPlayerConfig = null;
                }
            }
        }

        return videoPlayerConfig;
    }

    private void setDefaultParameterValues(final VideoStreamInfoMapper generator, final ScheduleItem item) {
        String defaultBufferingInterval = configurationItemsPort.getDefaultBufferingInterval(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId());

        if (StringUtils.isNotBlank(defaultBufferingInterval) && isStringPositiveNumber(defaultBufferingInterval)) {
            generator.setDefaultBufferingValue(defaultBufferingInterval);
        }

        String defaultVideoQuality = configurationItemsPort.getDefaultVideoQuality(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId());

        if (StringUtils.isNotBlank(defaultVideoQuality) && isStringConvertibleToEnumValue(VideoQuality.getValidValues(), defaultVideoQuality)) {
            generator.setDefaultVideoQuality(VideoQuality.valueOf(defaultVideoQuality));
        }
    }

    public static ContentType convertStreamTypeIdToContentType(Integer streamTypeId) {
        if (TypeStream.VID.getId() == streamTypeId) {
            return ContentType.VID;
        } else if (TypeStream.VIZ.getId() == streamTypeId) {
            return ContentType.VIZ;
        } else if (TypeStream.PRE_VID.getId() == streamTypeId) {
            return ContentType.PRE_VID;
        } else {
            return null;
        }
    }

    private boolean isStringPositiveNumber(String checkingValue) {
        return NumberUtils.isNumber(checkingValue) && !checkingValue.startsWith("-");
    }

    private boolean isStringConvertibleToEnumValue(Set<VideoQuality> validValues, String checkingValue) {
        Iterator var2 = validValues.iterator();

        Enum enumValue;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            enumValue = (Enum) var2.next();
        } while (!enumValue.name().equals(checkingValue));

        return true;
    }

}
