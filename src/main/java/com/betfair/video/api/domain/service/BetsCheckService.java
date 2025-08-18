package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_NO_BETS) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.BBV_NO_STAKES, String.valueOf(item.betfairSportsType()));
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_INSUFFICIENT_FUNDS) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_FUNDING, String.valueOf(item.betfairSportsType()));
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.WARN, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_DEPENDENT_SERVICE_ERROR) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, String.valueOf(item.betfairSportsType()));
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.ERROR, context, user, exception, null);
            throw exception;
        }

        if (bbvStatus == BetsCheckerStatusEnum.BBV_FAILED_TECHNICAL_ERROR) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
            StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.ERROR, context, user, exception, null);
            throw exception;
        }

        // If we reach here, it means the status is not recognized
        VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.GENERIC_ERROR, String.valueOf(item.betfairSportsType()));
        StreamExceptionLoggingUtils.logException(additionalInfoLoggingEnabled, logger, item.videoItemId(), Level.ERROR, context, user, exception, null);
        throw exception;
    }

}
