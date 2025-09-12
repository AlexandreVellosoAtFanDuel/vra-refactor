package com.betfair.video.domain.service;

import com.betfair.video.domain.dto.entity.RequestContext;
import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.dto.entity.User;
import com.betfair.video.domain.dto.search.VideoRequestIdentifier;
import com.betfair.video.domain.dto.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.domain.exception.BBVInsufficientStakesException;
import com.betfair.video.domain.exception.BBVNoStakesException;
import com.betfair.video.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.domain.exception.InsufficientFundingException;
import com.betfair.video.domain.exception.VideoException;
import com.betfair.video.domain.utils.StreamExceptionLoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;

@Service
public class BetsCheckService {

    private static final Logger logger = LoggerFactory.getLogger(BetsCheckService.class);

    private final StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    public BetsCheckService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        this.streamExceptionLoggingUtils = streamExceptionLoggingUtils;
    }

    public BetsCheckerStatusEnum getBBVStatus(VideoRequestIdentifier identifier, ScheduleItem item, RequestContext context) {

        if (context.user().isSuperUser()) {
            // Check if a user is a superuser
            logger.info("getBBVStatus: the user is a superUser, userId={}, item={}", context.user().userId(), item.videoItemId());

            final BetsCheckerStatusEnum status = BetsCheckerStatusEnum.BBV_NOT_REQUIRED_SUPERUSER;
            writeBBVLog(context, item, identifier, status);
            return status;
        }

        return BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG;
    }

    public void validateBBVStatus(BetsCheckerStatusEnum bbvStatus, ScheduleItem item, RequestContext context) {
        if (bbvStatus == BetsCheckerStatusEnum.BBV_PASSED
                || bbvStatus == BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG
                || bbvStatus == BetsCheckerStatusEnum.BBV_NOT_REQUIRED_SUPERUSER) {
            // BBV passed
            return;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_INSUFFICIENT_STAKES) {
            BBVInsufficientStakesException exception = new BBVInsufficientStakesException(String.valueOf(item.betfairSportsType()));
            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.WARN, context, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_NO_BETS) {
            BBVNoStakesException exception = new BBVNoStakesException(String.valueOf(item.betfairSportsType()));
            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.WARN, context, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_INSUFFICIENT_FUNDS) {
            InsufficientFundingException exception = new InsufficientFundingException(String.valueOf(item.betfairSportsType()));
            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.WARN, context, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_DEPENDENT_SERVICE_ERROR) {
            ErrorInDependentServiceException exception = new ErrorInDependentServiceException(String.valueOf(item.betfairSportsType()));
            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.ERROR, context, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_TECHNICAL_ERROR) {
            VideoException exception = new VideoException(VideoException.ErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.ERROR, context, exception, null);
            throw exception;
        }

        // If we reach here, it means the status is not recognized
        VideoException exception = new VideoException(VideoException.ErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
        streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.ERROR, context, exception, null);
        throw exception;
    }

    private void writeBBVLog(RequestContext context, ScheduleItem item, VideoRequestIdentifier identifier, BetsCheckerStatusEnum status) {
        writeLogWithBbvStatusInfo(context, item, identifier, status);

        final User user = context.user();

        // TODO: Implement the missing fields
        logger.info("{}, {}, {}, {}, {}, {}, {}, {}, {}",
                user.accountId(), "user.applicationKey()", item.providerId(), item.providerEventId(),
                item.betfairSportsType(), item.videoChannelType(), "identifier.getBBVLogString()",
                user.geolocation().countryCode(), status);
    }

    private void writeLogWithBbvStatusInfo(RequestContext context, ScheduleItem item, VideoRequestIdentifier identifier, BetsCheckerStatusEnum status) {
        final User user = context.user();

        // TODO: Implement the missing fields
        logger.info("uuid - {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}",
                context.uuid(), user.accountId(), "user.getApplicationKey()", item.providerId(), item.providerEventId(),
                item.betfairSportsType(), item.videoChannelType(), "identifier.getBBVLogString()",
                user.geolocation().countryCode(), user.geolocation().subDivisionCode(), status);
    }

}
