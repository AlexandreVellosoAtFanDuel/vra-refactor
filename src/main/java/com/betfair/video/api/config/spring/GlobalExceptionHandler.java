package com.betfair.video.api.config.spring;

import com.betfair.video.api.infra.input.rest.exception.ResponseCode;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIException;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.exception.StreamNotStartedException;
import com.betfair.video.api.infra.input.rest.dto.ErrorResponseDetailDto;
import com.betfair.video.api.infra.input.rest.dto.VideoApiExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String X_UUID = "X-UUID";

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(StreamNotStartedException.class)
    public ResponseEntity<Map<String, Object>> onStreamNotStartedException(StreamNotStartedException ex, HttpServletRequest request) {

        // TODO; Validate the logic here
        final String uuid = request.getHeader(X_UUID);

        VideoAPIException exception = new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_STARTED, ex.getSportType());

        logger.error("[{}]: VideoAPIException occurred: {} - {}", uuid, exception.getErrorCode(), ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(VideoAPIException.class)
    public ResponseEntity<Map<String, Object>> handleVideoAPIException(VideoAPIException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logger.error("[{}]: VideoAPIException occurred: {} - {}", uuid, ex.getErrorCode(), ex.getMessage());

        HttpStatus httpStatus = mapErrorCodeToHttpStatus(ex.getErrorCode());

        Map<String, Object> errorResponse = createErrorResponse(ex);

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        VideoAPIException exception = new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, ex.getParameterName());

        Map<String, Object> errorResponse = createErrorResponse(exception);

        logger.warn("[{}]: Missing parameter: {}", uuid, ex.getParameterName());

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logger.error("[{}]: An unexpected error occurred: {}", uuid, ex.getMessage(), ex);

        VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.UNRECOGNIZED_VALUE, null);

        Map<String, Object> errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus mapErrorCodeToHttpStatus(VideoAPIExceptionErrorCodeEnum errorCode) {
        return switch (errorCode) {
            case CONDITIONAL_DATA_MISSING,
                    INVALID_INPUT,
                    NO_SESSION,
                    UNKNOWN_CONSUMER -> BAD_REQUEST;

            case INSUFFICIENT_ACCESS -> HttpStatus.UNAUTHORIZED;

            case RESTRICTED_COUNTRY,
                    INSUFFICIENT_FUNDING,
                    BBV_INSUFFICIENT_STAKES,
                    BBV_NO_STAKES,
                    STREAM_ACCESS_DENIED,
                    USAGE_LIMITS_BREACHED,
                    PROVIDER_NOT_AVAILABLE_ON_SITE,
                    VENUE_NOT_AVAILABLE_ON_SITE -> HttpStatus.FORBIDDEN;

            case STREAM_NOT_FOUND,
                    STREAM_HAS_ENDED,
                    CANNOT_UNIQUELY_RESOLVE_STREAM,
                    NOT_IN_ARCHIVE_YET -> HttpStatus.NOT_FOUND;

            case STREAM_NOT_STARTED -> HttpStatus.OK;

            case COLD_STATE -> HttpStatus.SERVICE_UNAVAILABLE;

            case STREAM_GENERIC_ERROR,
                    ERROR_IN_DEPENDENT_SERVICE,
                    PROVIDER_CONNECTION_ERROR,
                    GENERIC_ERROR,
                    UNRECOGNIZED_VALUE -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private Map<String, Object> createErrorResponse(VideoAPIException ex) {
        VideoApiExceptionDto videoApiExceptionDto = new VideoApiExceptionDto(ex.getSportType(), ex.getErrorCode());
        ErrorResponseDetailDto responseDetailDto = new ErrorResponseDetailDto(videoApiExceptionDto, ex.getClass().getSimpleName());

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("faultcode", ex.getResponseCode().getFaultCode());
        errorResponse.put("faultstring", "DSC-????");
        errorResponse.put("detail", responseDetailDto);

        return errorResponse;
    }

}