package com.betfair.video.api.application.config;

import com.betfair.video.api.application.dto.ErrorResponseDetailDto;
import com.betfair.video.api.application.dto.VideoApiExceptionDto;
import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class VideoAPIExceptionHandler {

    @ExceptionHandler(VideoAPIException.class)
    public ResponseEntity<Map<String, Object>> handleVideoAPIException(VideoAPIException ex, HttpServletRequest request) {

        HttpStatus httpStatus = mapErrorCodeToHttpStatus(ex.getErrorCode());

        Map<String, Object> errorResponse = createErrorResponse(ex);

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {

        VideoAPIException exception = new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.UNRECOGNIZED_VALUE);

        Map<String, Object> errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus mapErrorCodeToHttpStatus(VideoAPIExceptionErrorCodeEnum errorCode) {
        return switch (errorCode) {
            case CONDITIONAL_DATA_MISSING,
                    INVALID_INPUT,
                    NO_SESSION,
                    UNKNOWN_CONSUMER -> HttpStatus.BAD_REQUEST;

            case INSUFFICIENT_ACCESS,
                    RESTRICTED_COUNTRY,
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