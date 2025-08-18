package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.mapper.ScheduleItemMapper;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.VideoStreamInfoPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.utils.ScheduleItemUtils;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import com.betfair.video.api.domain.valueobject.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

@Service
public class ScheduleItemService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleItemService.class);

    private static final String EXCHANGE_RACE_ID_TIME_SEPARATOR = ".";

    @Value("${videoapi.additional.info.enabled}")
    private boolean additionalInfoLoggingEnabled;

    private final VideoStreamInfoPort videoStreamInfoPort;

    private final PermissionService permissionService;

    private final ConfigurationItemsPort configurationItemsPort;

    public ScheduleItemService(VideoStreamInfoPort videoStreamInfoPort, PermissionService permissionService, ConfigurationItemsPort configurationItemsPort) {
        this.videoStreamInfoPort = videoStreamInfoPort;
        this.permissionService = permissionService;
        this.configurationItemsPort = configurationItemsPort;
    }

    public VideoStreamState getVideoStreamStateBasedOnScheduleItem(ScheduleItem item) {
        Date itemStartDate = item.getActualProviderData().start();
        if (item.leadTime() != null) {
            // Subtract the lead time from the start time
            itemStartDate = DateUtils.shiftDateByField(itemStartDate, Calendar.SECOND, -item.leadTime());
        }
        Date itemEndDate = item.getActualProviderData().end();
        if (itemEndDate != null && item.trailTime() != null) {
            // Add the trail time to item end time
            itemEndDate = DateUtils.shiftDateByField(itemEndDate, Calendar.SECOND, item.trailTime());
        }

        Date now = DateUtils.getCurrentDate();

        if (now.before(itemStartDate)) {
            // Stream has not started yet
            return VideoStreamState.NOT_STARTED;
        }

        if (itemEndDate != null && now.after(itemEndDate)) {
            // Stream has ended
            return VideoStreamState.FINISHED;
        }

        return VideoStreamState.STREAM;
    }

    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper wrapper, RequestContext context, User user) {
        final VideoStreamInfoByExternalIdSearchKey searchKey = wrapper.getVideoStreamInfoByExternalIdSearchKey();

        if (searchKey != null) {
            logger.info("[{}]: Attempting to retrieve schedule items for accountId={} by externalIdSource={}, primaryId={}, secondaryId={}",
                    context.uuid(), user.accountId(), searchKey.getExternalIdSource(), searchKey.getPrimaryId(), searchKey.getSecondaryId());
        }

        ScheduleItem item = null;

        final ExternalIdSource source = wrapper.getExternalIdSource();

        if (source != null) {
            switch (source) {
                case ExternalIdSource.BETFAIR_VIDEO,
                        ExternalIdSource.BETFAIR_MARKET,
                        ExternalIdSource.TIMEFORM,
                        ExternalIdSource.EXCHANGE_RACE,
                        ExternalIdSource.RAMP ->
                        throw new IllegalArgumentException(String.format("External ID source not implemented: %s", source.getExternalIdDescription()));
                case ExternalIdSource.BETFAIR_EVENT -> item = getBetfairEventItem(searchKey, wrapper, context, user);
            }
        }

        checkItemsProviderAllowedForAppkey(item, user);
        checkItemsVenueAllowedForAppkey(item, user);

        return item;
    }

    public void checkIsCurrentlyShowingAndThrow(VideoStreamState streamState, Long videoItemId, RequestContext context, User user, Integer sportType) {
        if (VideoStreamState.NOT_STARTED.equals(streamState)) {
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, videoItemId, Level.WARN, context, user,
                    new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_STARTED, String.valueOf(sportType)), null);
        }
        if (VideoStreamState.FINISHED.equals(streamState)) {
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, videoItemId, Level.WARN, context, user,
                    new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_HAS_ENDED, String.valueOf(sportType)), null);
        }
    }

    public boolean isItemWatchAndBetSupported(ScheduleItem scheduleItem) {
        //612164 Perform provider became watch&bet(no bbv check required for user) for horseracing, but only UK racecources are free, Irish venues we need to check using bbv services.
        // Remove this when Irish races are watch&bet
        String watchAndBetVenues = configurationItemsPort.findProviderWatchAndBetVenues(
                scheduleItem.providerId(), scheduleItem.videoChannelType(), scheduleItem.betfairSportsType(),
                scheduleItem.streamTypeId(), scheduleItem.brandId());

        return StringUtils.isNotBlank(watchAndBetVenues) && scheduleItem.providerData().venue() != null &&
                getStringValuesSeparatedByComma(watchAndBetVenues).stream()
                        .map(String::toLowerCase)
                        .toList()
                        .contains(scheduleItem.providerData().venue().toLowerCase());
    }

    private ScheduleItem getBetfairEventItem(VideoStreamInfoByExternalIdSearchKey searchKey, VideoStreamInfoSearchKeyWrapper wrapper, RequestContext context, User user) {
        VideoRequestIdentifier eventIdentifier = new VideoRequestIdentifier(
                null,
                searchKey.getPrimaryId(),
                null,
                null,
                null,
                null
        );

        List<ScheduleItem> items;
        try {
            items = videoStreamInfoPort.getVideoStreamInfoByExternalId(searchKey);
        } catch (final DataIsNotReadyException e) {
            logger.error("[{}]: {}", context.uuid(), e.getMessage());
            throw new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.COLD_STATE, null);
        }

        logger.info("[{}]: found  [{}] items - {}", context.uuid(), items.size(), ScheduleItemUtils.getItemsForLog(items));

        wrapper.setVideoRequestIdentifier(eventIdentifier);

        ScheduleItem item = permissionService.filterScheduleItems(user, items, searchKey, eventIdentifier);

        putRequestedEventInfoIntoContext(item, searchKey, context);

        return item;
    }

    private void checkItemsProviderAllowedForAppkey(ScheduleItem item, User user) {
    }

    private void checkItemsVenueAllowedForAppkey(ScheduleItem item, User user) {
    }

    private void putRequestedEventInfoIntoContext(ScheduleItem item, VideoStreamInfoByExternalIdSearchKey searchKey, RequestContext context) {
        if (item != null && item.mappings() != null && !item.mappings().isEmpty()) {
            ScheduleItemMapper mapping = filterMappingsByExternalId(item, searchKey, context);

            if (mapping != null) {
                if (mapping.getScheduleItemMappingKey() != null && mapping.getScheduleItemMappingKey().providerEventKey() != null) {
                    /*
                    TODO:
                    user.getCurrentContextHolder().params.put(User.CurrentContextHolder.PARAM_STREAM_REQUESTED_EVENT_ID,
                            mapping.getScheduleItemMappingKey().getProviderEventKey().getPrimaryId());
                    user.getCurrentContextHolder().params.put(User.CurrentContextHolder.PARAM_STREAM_MAPPING_DESCRIPTION,
                            CommonUtils.removeCommas(CommonUtils.removeLineBrakes(mapping.getMappingDescription())));
                    user.getCurrentContextHolder().params.put(User.CurrentContextHolder.EXCHANGE_RACE_ID,
                            mapping.getExchangeRaceId());
                    */
                }
            }
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
            boolean isRequestedMapping = isRequestedMapping(mapping, byExternalIdSearchKey, externalId);

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

        if (ExternalIdSource.EXCHANGE_RACE.getExternalIdSource().equals(byExternalIdSearchKey.getExternalIdSource().getExternalIdSource())
                && externalId.contains(EXCHANGE_RACE_ID_TIME_SEPARATOR)) {
            //for "by race id" request its possible that >1 mappings has same race id, so we need to pick one
            String eventIdExtractedFromRaceId = externalId.substring(0, externalId.indexOf(EXCHANGE_RACE_ID_TIME_SEPARATOR));

            for (ScheduleItemMapper mapping : filteredMappings) {
                String mappingEventId = mapping.getScheduleItemMappingKey().providerEventKey().primaryId();
                if (eventIdExtractedFromRaceId.equals(mappingEventId)) {
                    pickedMapping = mapping;
                    break;
                }
            }
        } else {
            logger.error("[{}]: has >1 mappings for schedule item, will pick 1st available one to " +
                            "extract event id. This is not critical but should not happen. Search key: {}. Filtered mappings: {}",
                    context.uuid(), searchKey, filteredMappings);
        }

        return pickedMapping;
    }

    private boolean isRequestedMapping(ScheduleItemMapper mapping, VideoStreamInfoByExternalIdSearchKey byExternalIdSearchKey, String externalId) {
        boolean isRequestedMapping;

        if (ExternalIdSource.EXCHANGE_RACE.getExternalIdSource().equals(byExternalIdSearchKey.getExternalIdSource().getExternalIdSource())) {
            isRequestedMapping = externalId.equals(mapping.getExchangeRaceId());
        } else if (ExternalIdSource.RAMP.getExternalIdSource().equals(byExternalIdSearchKey.getExternalIdSource().getExternalIdSource())) {
            isRequestedMapping = externalId.equals(mapping.getRampId());
        } else {
            isRequestedMapping = externalId.equals(mapping.getScheduleItemMappingKey().providerEventKey().primaryId());
        }

        return isRequestedMapping;
    }

    private List<String> getStringValuesSeparatedByComma(String valueList) {
        Set<String> stringSet = new HashSet<>();
        if (StringUtils.isNotBlank(valueList)) {
            StringTokenizer st = new StringTokenizer(valueList, ",");

            while(st.hasMoreTokens()) {
                stringSet.add(st.nextToken());
            }
        }

        return new ArrayList<>(stringSet);
    }

}
