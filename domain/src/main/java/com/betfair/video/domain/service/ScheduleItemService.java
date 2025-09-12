package com.betfair.video.domain.service;

import com.betfair.video.domain.dto.entity.RequestContext;
import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.dto.entity.User;
import com.betfair.video.domain.dto.search.VideoRequestIdentifier;
import com.betfair.video.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.domain.dto.search.VideoStreamInfoSearchKeyWrapper;
import com.betfair.video.domain.dto.valueobject.VideoStreamState;
import com.betfair.video.domain.exception.ColdStateException;
import com.betfair.video.domain.exception.DataIsNotReadyException;
import com.betfair.video.domain.exception.StreamHasEndedException;
import com.betfair.video.domain.exception.StreamNotStartedException;
import com.betfair.video.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.domain.port.output.VideoStreamInfoPort;
import com.betfair.video.domain.utils.DateUtils;
import com.betfair.video.domain.utils.ScheduleItemUtils;
import com.betfair.video.domain.utils.StreamExceptionLoggingUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
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

    private final StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    private final VideoStreamInfoPort videoStreamInfoPort;

    private final PermissionService permissionService;

    private final ConfigurationItemsPort configurationItemsPort;

    public ScheduleItemService(StreamExceptionLoggingUtils streamExceptionLoggingUtils, VideoStreamInfoPort videoStreamInfoPort,
                               PermissionService permissionService, ConfigurationItemsPort configurationItemsPort) {
        this.streamExceptionLoggingUtils = streamExceptionLoggingUtils;
        this.videoStreamInfoPort = videoStreamInfoPort;
        this.permissionService = permissionService;
        this.configurationItemsPort = configurationItemsPort;
    }

    public VideoStreamState getVideoStreamStateBasedOnScheduleItem(ScheduleItem item) {
        Date itemStartDate = item.getActualProviderData().getStart();
        if (item.leadTime() != null) {
            // Subtract the lead time from the start time
            itemStartDate = DateUtils.shiftDateByField(itemStartDate, Calendar.SECOND, -item.leadTime());
        }
        Date itemEndDate = item.getActualProviderData().getEnd();
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

    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper wrapper, RequestContext context) {
        final VideoStreamInfoByExternalIdSearchKey searchKey = wrapper.getVideoStreamInfoByExternalIdSearchKey();

        if (searchKey != null) {
            logger.info("[{}]: Attempting to retrieve schedule items for accountId={} by externalIdSource={}, primaryId={}, secondaryId={}",
                    context.uuid(), context.user().accountId(), searchKey.getExternalIdSource(), searchKey.getPrimaryId(), searchKey.getSecondaryId());
        }

        ScheduleItem item = getBetfairEventItem(searchKey, wrapper, context);

        checkItemsProviderAllowedForAppkey(item, context.user());
        checkItemsVenueAllowedForAppkey(item, context.user());

        return item;
    }

    public void checkIsCurrentlyShowingAndThrow(VideoStreamState streamState, Long videoItemId, RequestContext context, Integer sportType) {
        if (VideoStreamState.NOT_STARTED.equals(streamState)) {
            StreamNotStartedException exception = new StreamNotStartedException(String.valueOf(sportType));
            streamExceptionLoggingUtils.logException(logger, videoItemId, Level.WARN, context, exception, null);
            throw exception;
        }

        if (VideoStreamState.FINISHED.equals(streamState)) {
            StreamHasEndedException exception = new StreamHasEndedException(String.valueOf(sportType));
            streamExceptionLoggingUtils.logException(logger, videoItemId, Level.WARN, context, exception, null);
            throw exception;
        }
    }

    public boolean isItemWatchAndBetSupported(ScheduleItem scheduleItem) {
        //612164 Perform provider became watch&bet(no bbv check required for user) for horseracing, but only UK racecources are free, Irish venues we need to check using bbv services.
        // Remove this when Irish races are watch&bet
        String watchAndBetVenues = configurationItemsPort.findProviderWatchAndBetVenues(
                scheduleItem.providerId(), scheduleItem.videoChannelType(), scheduleItem.betfairSportsType(),
                scheduleItem.streamTypeId(), scheduleItem.brandId());

        return StringUtils.isNotBlank(watchAndBetVenues) && scheduleItem.providerData().getVenue() != null &&
                getStringValuesSeparatedByComma(watchAndBetVenues).stream()
                        .map(String::toLowerCase)
                        .toList()
                        .contains(scheduleItem.providerData().getVenue().toLowerCase());
    }

    private ScheduleItem getBetfairEventItem(VideoStreamInfoByExternalIdSearchKey searchKey, VideoStreamInfoSearchKeyWrapper wrapper, RequestContext context) {
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
            throw new ColdStateException("There are no video stream available at the moment. Please try later.");
        }

        logger.info("[{}]: found  [{}] items - {}", context.uuid(), items.size(), ScheduleItemUtils.getItemsForLog(items));

        wrapper.setVideoRequestIdentifier(eventIdentifier);

        return permissionService.filterScheduleItems(context, items, searchKey, eventIdentifier);
    }

    private void checkItemsProviderAllowedForAppkey(ScheduleItem item, User user) {
    }

    private void checkItemsVenueAllowedForAppkey(ScheduleItem item, User user) {
    }

    private List<String> getStringValuesSeparatedByComma(String valueList) {
        Set<String> stringSet = new HashSet<>();
        if (StringUtils.isNotBlank(valueList)) {
            StringTokenizer st = new StringTokenizer(valueList, ",");

            while (st.hasMoreTokens()) {
                stringSet.add(st.nextToken());
            }
        }

        return new ArrayList<>(stringSet);
    }

    public boolean itemIsAvailable(ScheduleItem item, User user) {
        // TODO: Implement method
        return true;
    }

    public String filterItemsAgainstContentTypesAndProviders(List<ScheduleItem> items, VideoStreamInfoByExternalIdSearchKey searchKey, User user) {
        // TODO: Implement method
        return null;
    }

    public ScheduleItem getItemWithHighestProviderEventId(List<ScheduleItem> items) {
        // TODO: Implement method
        return null;
    }
}
