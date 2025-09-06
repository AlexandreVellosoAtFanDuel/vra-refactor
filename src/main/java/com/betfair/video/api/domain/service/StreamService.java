package com.betfair.video.api.domain.service;

import com.betfair.video.api.infra.input.rest.exception.ResponseCode;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIException;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.TypeSport;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.mapper.ScheduleItemMapper;
import com.betfair.video.api.domain.mapper.VideoStreamInfoMapper;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.DirectStreamConfigPort;
import com.betfair.video.api.domain.port.InlineStreamConfigPort;
import com.betfair.video.api.domain.port.ProviderFactoryPort;
import com.betfair.video.api.domain.port.ReferenceTypePort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.ContentType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeEnum;
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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class StreamService {

    private static final Logger logger = LoggerFactory.getLogger(StreamService.class);

    public static final Pattern LINEBREAKS_PATTERN = Pattern.compile("(\r)|(\n)");

    public static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private final ConfigurationItemsPort configurationItemsPort;

    private final ScheduleItemService scheduleItemService;

    private final ProviderFactoryPort providerFactoryPort;

    private final PermissionService permissionService;

    private final BetsCheckService betsCheckService;

    private final DirectStreamConfigPort directStreamConfigPort;

    private final InlineStreamConfigPort inlineStreamConfigPort;

    private final GeoRestrictionsService geoRestrictionsService;

    private final VideoStreamInfoMapper videoStreamInfoMapper;

    private final ReferenceTypePort referenceTypePort;

    public StreamService(ConfigurationItemsPort configurationItemsPort,
                         ScheduleItemService scheduleItemService, ProviderFactoryPort providerFactoryPort,
                         PermissionService permissionService, BetsCheckService betsCheckService,
                         DirectStreamConfigPort directStreamConfigPort, InlineStreamConfigPort inlineStreamConfigPort,
                         GeoRestrictionsService geoRestrictionsService, VideoStreamInfoMapper videoStreamInfoMapper, ReferenceTypePort referenceTypePort) {
        this.configurationItemsPort = configurationItemsPort;
        this.scheduleItemService = scheduleItemService;
        this.providerFactoryPort = providerFactoryPort;
        this.permissionService = permissionService;
        this.betsCheckService = betsCheckService;
        this.directStreamConfigPort = directStreamConfigPort;
        this.inlineStreamConfigPort = inlineStreamConfigPort;
        this.geoRestrictionsService = geoRestrictionsService;
        this.videoStreamInfoMapper = videoStreamInfoMapper;
        this.referenceTypePort = referenceTypePort;
    }

    public VideoStreamInfo getStreamInfoByExternalId(final VideoStreamInfoByExternalIdSearchKey searchKey, final RequestContext context, final boolean includeMetadata) {

        if (searchKey.getProviderId() != null && referenceTypePort.findReferenceTypeById(searchKey.getProviderId(), ReferenceTypeEnum.VIDEO_PROVIDER) == null) {
            logger.error("[{}]: No provider was found for the given provider ID ({}). User country: {},{}.",
                    context.uuid(), searchKey.getProviderId(), context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper = new VideoStreamInfoSearchKeyWrapper(
                searchKey.getExternalIdSource(),
                searchKey,
                null,
                null
        );

        ScheduleItem item = scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, context);

        //try to find leading stream if current is not live yet
        ScheduleItem paddockItem = tryFindLeadingStream(item, searchKey.getPrimaryId(), videoStreamInfoSearchKeyWrapper, context);
        if (paddockItem != null) {
            logger.info("[{}]: Found leading stream for external id {}. Video id: {}. User country: {},{}",
                    context.uuid(), searchKey.getPrimaryId(), paddockItem.videoItemId(), context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode());

            item = paddockItem;
        }

        populateCurrentRequestContextParams(context.user(), item, searchKey.getExternalIdSource().getExternalIdDescription(),
                searchKey.getPrimaryId(), String.valueOf(item.videoItemId()));

        VideoRequestIdentifier identifier = videoStreamInfoSearchKeyWrapper.getVideoRequestIdentifier(item);

        // A single video item was found
        validateScheduleItem(identifier, item, context);

        StreamingProviderPort provider = providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(item.providerId());

        return getStreamInfoForItem(item, provider, searchKey, context, includeMetadata);
    }

    private ScheduleItem tryFindLeadingStream(ScheduleItem item, String externalId, VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper,
                                              RequestContext context) throws VideoAPIException {

        boolean isStreamTypeAllowed = configurationItemsPort.isStreamTypeAllowed(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), TypeStream.PRE_VID, item.brandId());

        if (!isStreamTypeAllowed) {
            return null;
        }

        VideoStreamState streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item);

        if (VideoStreamState.NOT_STARTED.equals(streamState)) {
            logger.info("[{}]: Stream by externalId {} is not live yet. Trying to find pre-event paddock stream. User country: {},{}",
                    context.uuid(), externalId, context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode());

            videoStreamInfoSearchKeyWrapper.getVideoStreamInfoByExternalIdSearchKey()
                    .setStreamTypeIds(Collections.singleton(TypeStream.PRE_VID.getId()));

            videoStreamInfoSearchKeyWrapper.getVideoStreamInfoByExternalIdSearchKey().setProviderId(item.providerId());

            try {
                return scheduleItemService.getScheduleItemByStreamKey(videoStreamInfoSearchKeyWrapper, context);
            } catch (VideoAPIException e) {
                //if paddock stream doesn't exist or any other error - return null
                logger.info("[{}]: Got {} error while trying to find paddock stream for externalId {}. Returning null. Parent stream will be processed further. User country: {},{}",
                        context.uuid(), e.getErrorCode(), externalId, context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode());

                return null;
            }
        }

        return null;
    }

    private void populateCurrentRequestContextParams(User user, ScheduleItem item, String requestedStreamIdSource,
                                                     String requestedStreamId, String videoItemId) {
    }

    private void validateScheduleItem(VideoRequestIdentifier identifier, ScheduleItem item, RequestContext context) {

        StreamingProviderPort provider = providerFactoryPort.getStreamingProviderByIdAndVideoChannelId(item.providerId());

        if (provider == null) {
            logger.error("[{}]: No provider was found for the given provider ID ({}).",
                    context.uuid(), item.providerId());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        if (!provider.isEnabled()) {
            logger.error("[{}]: Provider with ID {} is not enabled.", context.uuid(), item.providerId());
            throw new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, null);
        }

        boolean userHasPermissionAgainstItem = permissionService.checkUserPermissionsAgainstItem(item, context.user());

        if (!userHasPermissionAgainstItem) {
            logger.warn("[{}]: User {} does not have permission to access stream for item with videoItemId {}. User country: {},{}",
                    context.uuid(), context.user().accountId(), item.videoItemId(), context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode());
            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
        }

        // Make sure that the video is currently showing before proceed
        checkStreamStatus(item, context);

        BetsCheckerStatusEnum bbvStatus = scheduleItemService.isItemWatchAndBetSupported(item)
                ? BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG
                : betsCheckService.getBBVStatus(identifier, item, context);

        // Validate BBV status, if it is not valid an exception is thrown
        betsCheckService.validateBBVStatus(bbvStatus, item, context);
    }

    private void checkStreamStatus(ScheduleItem item, RequestContext context) throws VideoAPIException {
        VideoStreamState streamState = scheduleItemService.getVideoStreamStateBasedOnScheduleItem(item);

        if (item.streamTypeId().equals(TypeStream.PRE_VID.getId()) && VideoStreamState.FINISHED.equals(streamState)) {
            //in case of finished paddock we should return NOT_STARTED because in means that we are currently inside a window
            //between end of paddock and before start of event stream
            streamState = VideoStreamState.NOT_STARTED;
        }

        scheduleItemService.checkIsCurrentlyShowingAndThrow(streamState, item.videoItemId(), context, item.betfairSportsType());
    }

    private VideoStreamInfo getStreamInfoForItem(ScheduleItem item, StreamingProviderPort provider, VRAStreamSearchKey searchKey, RequestContext context,
                                                 boolean includeMetadata) throws VideoAPIException {

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

        StreamDetails streamDetails = provider.getStreamDetails(context, item, params);

        boolean isDirectStream = directStreamConfigPort.isProviderInList(item.providerId(), item.videoChannelType());

        boolean isInlineStream = isDirectStream && inlineStreamConfigPort.isProviderInList(item.providerId(), item.videoChannelType());

        String eventId = null;
        String eventName = null;
        String exchangeRaceId = null;

        if (item.mappings() != null && !item.mappings().isEmpty()) {
            ScheduleItemMapper mapping = filterMappingsByExternalId(item, searchKey, context);

            if (mapping != null && mapping.scheduleItemMappingKey() != null && mapping.scheduleItemMappingKey().providerEventKey() != null) {
                eventId = mapping.scheduleItemMappingKey().providerEventKey().primaryId();
                eventName = removeCommas(removeLineBrakes(mapping.mappingDescription()));
                exchangeRaceId = mapping.exchangeRaceId();
            }
        }

        return videoStreamInfoMapper.map(
                item,
                streamDetails,
                provider.getAvailableVideoQualityValues(),
                configurationItemsPort.getSizeRestrictions(item.providerId(), item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId()),
                isDirectStream,
                isInlineStream,
                convertStreamTypeIdToContentType(item.streamTypeId()),
                TypeSport.getById(item.betfairSportsType()),
                getDefaultVideoQuality(item),
                getDefaultBufferingValue(item),
                context.user(),
                includeMetadata,
                getVideoPlayerConfig(item, streamDetails),
                geoRestrictionsService,
                eventId,
                eventName,
                exchangeRaceId
        );
    }

    private String removeCommas(String string) {
        if (string != null) {
            Matcher m = COMMA_PATTERN.matcher(string);
            return m.replaceAll("");
        } else {
            return null;
        }
    }

    private String removeLineBrakes(String string) {
        if (string != null) {
            Matcher m = LINEBREAKS_PATTERN.matcher(string);
            return m.replaceAll("");
        } else {
            return null;
        }
    }

    private ScheduleItemMapper filterMappingsByExternalId(ScheduleItem item, VRAStreamSearchKey searchKey, RequestContext context) {
        Set<ScheduleItemMapper> filteredMappings = new HashSet<>();

        if (!(searchKey instanceof VideoStreamInfoByExternalIdSearchKey)) {
            return null;
        }

        VideoStreamInfoByExternalIdSearchKey byExternalIdSearchKey = (VideoStreamInfoByExternalIdSearchKey) searchKey;
        String externalId = byExternalIdSearchKey.getPrimaryId();

        if (externalId == null) {
            return null;
        }

        for (ScheduleItemMapper mapping : item.mappings()) {
            boolean isRequestedMapping = isRequestedMapping(mapping, externalId);

            if (isRequestedMapping) {
                filteredMappings.add(mapping);
            }
        }

        if (filteredMappings.isEmpty()) {
            return null;
        }

        if (filteredMappings.size() == 1) {
            return filteredMappings.iterator().next();
        }

        ScheduleItemMapper pickedMapping = filteredMappings.iterator().next();

        logger.error("[{}]: has >1 mappings for schedule item, will pick 1st available one to " +
                        "extract event id. This is not critical but should not happen. Search key: {}. Filtered mappings: {}",
                context.uuid(), searchKey, filteredMappings);

        return pickedMapping;
    }

    private boolean isRequestedMapping(ScheduleItemMapper mapping, String externalId) {
        return externalId.equals(mapping.scheduleItemMappingKey().providerEventKey().primaryId());
    }

    private String getVideoPlayerConfig(ScheduleItem scheduleItem, StreamDetails streamDetails) {
        String videoPlayerConfig = null;
        ConfigurationItem configurationItem = configurationItemsPort.findVideoPlayerConfig(scheduleItem.providerId(), scheduleItem.videoChannelType(),
                scheduleItem.betfairSportsType(), scheduleItem.streamTypeId(), scheduleItem.brandId());

        boolean configurationItemExist = configurationItem != null;
        if (configurationItemExist) {
            String streamFormat = streamDetails.params().get(StreamDetailsParamEnum.STREAM_FORMAT_PARAM_NAME.getParamName());
            boolean streamFormatExist = streamFormat != null && !streamFormat.isEmpty();

            String configValue = configurationItem.configValue();
            boolean configValueExist = configValue != null && !configValue.isEmpty();

            if (streamFormatExist && configValueExist) {
                videoPlayerConfig = getVideoPlayerConfigByStreamFormat(configValue, streamFormat);
            }
        }

        return videoPlayerConfig;
    }

    private String getVideoPlayerConfigByStreamFormat(String playerConfig, String streamFormat) {
        String playerConfigByStreamFormat = null;
        String[] configPerFormatArray = playerConfig.split("--");

        for (String formatConfig : configPerFormatArray) {
            boolean isCurrentStreamFormatConfig = formatConfig.contains(streamFormat);
            if (isCurrentStreamFormatConfig) {
                playerConfigByStreamFormat = retrievePlayerConfigValue(formatConfig);
            }
        }

        return playerConfigByStreamFormat;
    }

    private String retrievePlayerConfigValue(String formatConfig) {
        String playerConfigByStreamFormat = null;
        String[] formatAndConfigArray = formatConfig.split("::");
        boolean isCorrectFormatAndConfigArray = formatAndConfigArray.length > 1;
        if (isCorrectFormatAndConfigArray) {
            String configValue = formatAndConfigArray[1];
            if (isJsonFormat(configValue)) {
                playerConfigByStreamFormat = configValue;
            } else {
                logger.warn("Video player config format is not json : {}", formatConfig);
            }
        } else {
            logger.warn("Incorrect video player config format : {}", formatConfig);
        }

        return playerConfigByStreamFormat;
    }

    private static boolean isJsonFormat(String value) {
        return value.contains("{") && value.contains("}");
    }

    private String getDefaultBufferingValue(final ScheduleItem item) {
        String defaultBufferingInterval = configurationItemsPort.getDefaultBufferingInterval(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId());

        if (StringUtils.isNotBlank(defaultBufferingInterval) && isStringPositiveNumber(defaultBufferingInterval)) {
            return defaultBufferingInterval;
        }

        return null;
    }

    private VideoQuality getDefaultVideoQuality(final ScheduleItem item) {
        String defaultVideoQuality = configurationItemsPort.getDefaultVideoQuality(item.providerId(),
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId());

        if (StringUtils.isNotBlank(defaultVideoQuality) && VideoQuality.isValidValue(defaultVideoQuality)) {
            return VideoQuality.valueOf(defaultVideoQuality);
        }

        return null;
    }

    private static ContentType convertStreamTypeIdToContentType(Integer streamTypeId) {
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
        return NumberUtils.isCreatable(checkingValue) && !checkingValue.startsWith("-");
    }

}
