package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.utils.ScheduleItemUtils;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BetsCheckService {

    private static final Logger logger = LoggerFactory.getLogger(BetsCheckService.class);

    @Value("${videoapi.additional.info.enabled}")
    private boolean additionalInfoLoggingEnabled;

    public BetsCheckerStatusEnum getBBVStatus(VideoRequestIdentifier identifier, ScheduleItem item, User user, boolean isArchivedStream) {
        return BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG;
    }

    public void validateBBVStatus(BetsCheckerStatusEnum bbvStatus, ScheduleItem item, User user, RequestContext context) {
        if (bbvStatus == BetsCheckerStatusEnum.BBV_PASSED
                || bbvStatus == BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG
                || bbvStatus == BetsCheckerStatusEnum.BBV_NOT_REQUIRED_SUPERUSER) {
            // BBV passed
            return;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_INSUFFICIENT_STAKES) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.BBV_INSUFFICIENT_STAKES, String.valueOf(item.betfairSportsType()));
            logException(item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_NO_BETS) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.BBV_NO_STAKES, String.valueOf(item.betfairSportsType()));
            logException(item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_INSUFFICIENT_FUNDS) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_FUNDING, String.valueOf(item.betfairSportsType()));
            logException(item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_DEPENDENT_SERVICE_ERROR) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, String.valueOf(item.betfairSportsType()));
            logException(item.videoItemId(), Level.ERROR, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_TECHNICAL_ERROR) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
            logException(item.videoItemId(), Level.ERROR, context, user, exception, null);
            throw exception;
        }

        // If we reach here, it means the status is not recognized
        VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
        logException(item.videoItemId(), Level.ERROR, context, user, exception, null);
        throw exception;
    }

    private void logException(final Long videoId, final Level logLevel, final RequestContext context, final User user,
                              final VideoAPIException exception, final String addInfo) throws VideoAPIException {
        VideoRequestIdentifier videoRequestIdentifier = new VideoRequestIdentifier(videoId.toString(), null, null, null, null, null);

        logException(additionalInfoLoggingEnabled, videoRequestIdentifier, logLevel, context, user, exception, Collections.emptyList(), addInfo);
    }

    private void logException(final boolean additionalInfoLoggingEnabled,
                              VideoRequestIdentifier identifier, final Level logLevel, final RequestContext context, final User user,
                              final VideoAPIException exception, final List<ScheduleItem> items, final String addInfo) {
        String requestedSource = getRequestedSource(identifier);

        //build items info if provided
        StringBuilder itemsToLog = ScheduleItemUtils.getItemsForLog(items);

        if (additionalInfoLoggingEnabled && addInfo != null) {
            logger.atLevel(logLevel).log("[{}]: User {} {},{} tried to access a video stream by {} and encountered error: {} [{}]{} [Additional info: {}]",
                    context.uuid(), user.accountId(), user.geolocation().countryCode(), user.geolocation().subDivisionCode(), requestedSource, exception.getExceptionCode(), exception.getErrorCode(), itemsToLog, addInfo);
        } else {
            logger.atLevel(logLevel).log("[{}]: User {} {},{} tried to access a video stream by {} and encountered error: {} [{}]{}",
                    context.uuid(), user.accountId(), user.geolocation().countryCode(), user.geolocation().subDivisionCode(), requestedSource, exception.getExceptionCode(), exception.getErrorCode(), itemsToLog);
        }
    }

    private static String getRequestedSource(VideoRequestIdentifier identifier) {
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
