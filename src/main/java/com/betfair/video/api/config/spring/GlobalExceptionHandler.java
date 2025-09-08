package com.betfair.video.api.config.spring;

import com.betfair.video.api.domain.exception.BBVNoStakesException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.infra.input.rest.dto.VideoExceptionDto;
import com.betfair.video.api.domain.exception.StreamNotStartedException;
import com.betfair.video.api.infra.input.rest.dto.ErrorResponseDetailDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.betfair.video.api.domain.exception.VideoException.ErrorCodeEnum.INVALID_INPUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String X_UUID = "X-UUID";

    @Value("${videoapi.return.stacktrace.on.error}")
    private boolean returnStackTraceOnError;

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(StreamNotStartedException.class)
    public Map<String, Object> onStreamNotStartedException(StreamNotStartedException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(BBVNoStakesException.class)
    public Map<String, Object> onBBVNoStakesException(BBVNoStakesException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(VideoException.class)
    public Map<String, Object> onStreamNotStartedException(VideoException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.SERVER);
    }

    // TODO: Implement error handler for the following exceptions:
    // - BBVInsufficientStakesException
    // - ErrorInDependentServiceException
    // - InsufficientAccessException
    // - InsufficientFundingException
    // - InvalidInputException
    // - StreamNotFoundException
    // - CannotUniquelyResolveStreamException
    // - StreamHasEndedException
    // - ColdStateException
    // - UnknownConsumerException
    // - RestrictedCountryException

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> onGenericException(Exception ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logger.error("[{}]: An unexpected error occurred: {}", uuid, ex.getMessage(), ex);

        VideoException exception = new VideoException();

        return createErrorResponse(exception, FaultCode.SERVER);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        VideoException exception = new VideoException(INVALID_INPUT, ex.getParameterName(), null);

        Map<String, Object> errorResponse = createErrorResponse(exception, FaultCode.CLIENT);

        logger.warn("[{}]: Missing parameter: {}", uuid, ex.getParameterName());

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

//    private HttpStatus mapErrorCodeToHttpStatus(VideoAPIExceptionErrorCodeEnum errorCode) {
//        return switch (errorCode) {
//            case CONDITIONAL_DATA_MISSING,
//                    INVALID_INPUT,
//                    NO_SESSION,
//                    UNKNOWN_CONSUMER -> BAD_REQUEST;
//
//            case INSUFFICIENT_ACCESS -> HttpStatus.UNAUTHORIZED;
//
//            case RESTRICTED_COUNTRY,
//                    INSUFFICIENT_FUNDING,
//                    BBV_INSUFFICIENT_STAKES,
//                    BBV_NO_STAKES,
//                    STREAM_ACCESS_DENIED,
//                    USAGE_LIMITS_BREACHED,
//                    PROVIDER_NOT_AVAILABLE_ON_SITE,
//                    VENUE_NOT_AVAILABLE_ON_SITE -> HttpStatus.FORBIDDEN;
//
//            case STREAM_NOT_FOUND,
//                    STREAM_HAS_ENDED,
//                    CANNOT_UNIQUELY_RESOLVE_STREAM,
//                    NOT_IN_ARCHIVE_YET -> HttpStatus.NOT_FOUND;
//
//            case STREAM_NOT_STARTED -> HttpStatus.OK;
//
//            case COLD_STATE -> HttpStatus.SERVICE_UNAVAILABLE;
//
//            case STREAM_GENERIC_ERROR,
//                    ERROR_IN_DEPENDENT_SERVICE,
//                    PROVIDER_CONNECTION_ERROR,
//                    GENERIC_ERROR,
//                    UNRECOGNIZED_VALUE -> HttpStatus.INTERNAL_SERVER_ERROR;
//        };
//    }

    private void logError(String uuid, VideoException ex) {
        logger.error("[{}]: An unexpected error occurred: {}", uuid, ex.getMessage(), ex);
    }

    private Map<String, Object> createErrorResponse(VideoException ex, FaultCode faultCode) {
        VideoExceptionDto videoExceptionDto = new VideoExceptionDto(ex.getSportType(), ex.getErrorCode());

        return buildErrorResponse(ex, faultCode, videoExceptionDto);
    }

    private Map<String, Object> buildErrorResponse(VideoException ex, FaultCode faultCode, VideoExceptionDto videoExceptionDto) {
        ErrorResponseDetailDto responseDetailDto = new ErrorResponseDetailDto(
                videoExceptionDto,
                returnStackTraceOnError ? Arrays.toString(ex.getStackTrace()) : null,
                ex.getMessage(),
                ex.getClass().getSimpleName()
        );

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("faultcode", faultCode.getFaultCode());
        errorResponse.put("faultstring", ex.getErrorCode().getValue());
        errorResponse.put("detail", responseDetailDto);

        return errorResponse;
    }

    enum FaultCode {
        CLIENT("Client"),
        SERVER("Server");

        private final String faultCode;

        FaultCode(String faultCode) {
            this.faultCode = faultCode;
        }

        public String getFaultCode() {
            return faultCode;
        }

    }

}