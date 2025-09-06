package com.betfair.video.api.domain.utils;

import com.betfair.video.api.infra.input.rest.exception.VideoAPIException;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StreamExceptionLoggingUtils {

    @Value("${videoapi.additional.info.enabled}")
    private boolean additionalInfoLoggingEnabled;

    private StreamExceptionLoggingUtils() {
    }

    public void logException(Logger logger, final Long videoId, final Level logLevel, final RequestContext context,
                             final VideoAPIException exception, final String addInfo) throws VideoAPIException {
        VideoRequestIdentifier videoRequestIdentifier = new VideoRequestIdentifier(videoId.toString(), null, null, null, null, null);

        logException(logger, videoRequestIdentifier, logLevel, context, exception, Collections.emptyList(), addInfo);
    }

    public void logException(Logger logger,
                                     VideoRequestIdentifier identifier, final Level logLevel, final RequestContext context,
                                     final VideoAPIException exception, final List<ScheduleItem> items, final String addInfo) {
        String requestedSource = getRequestedSource(identifier);

        //build items info if provided
        StringBuilder itemsToLog = ScheduleItemUtils.getItemsForLog(items);

        if (additionalInfoLoggingEnabled && addInfo != null) {
            logger.atLevel(logLevel).log("[{}]: User {} {},{} tried to access a video stream by {} and encountered error: {} [{}]{} [Additional info: {}]",
                    context.uuid(), context.user().accountId(), context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode(), requestedSource, exception.getExceptionCode(), exception.getErrorCode(), itemsToLog, addInfo);
        } else {
            logger.atLevel(logLevel).log("[{}]: User {} {},{} tried to access a video stream by {} and encountered error: {} [{}]{}",
                    context.uuid(), context.user().accountId(), context.user().geolocation().countryCode(), context.user().geolocation().subDivisionCode(), requestedSource, exception.getExceptionCode(), exception.getErrorCode(), itemsToLog);
        }
    }

    private String getRequestedSource(VideoRequestIdentifier identifier) {
        String requestedSource;

        if (identifier != null) {
            if (identifier.marketId() != null) {
                requestedSource = String.format("market id:%s", identifier.marketId());
            } else if (identifier.eventId() != null) {
                requestedSource = String.format("event id:%s", identifier.eventId());
            } else if (identifier.timeformRaceId() != null) {
                requestedSource = String.format("timeform race id:%s", identifier.timeformRaceId());
            } else if (identifier.exchangeRaceId() != null) {
                requestedSource = String.format("exchange race id:%s", identifier.exchangeRaceId());
            } else if (identifier.rampId() != null) {
                requestedSource = String.format("ramp id:%s", identifier.rampId());
            } else {
                requestedSource = String.format("video id:%s", identifier.videoId());
            }
        } else {
            requestedSource = "unknown identifier";
        }

        return requestedSource;
    }

}
