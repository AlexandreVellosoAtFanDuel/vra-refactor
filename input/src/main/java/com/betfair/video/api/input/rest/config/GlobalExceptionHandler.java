package com.betfair.video.api.input.rest.config;

import com.betfair.video.api.domain.exception.BBVInsufficientStakesException;
import com.betfair.video.api.domain.exception.BBVNoStakesException;
import com.betfair.video.api.domain.exception.CannotUniquelyResolveStreamException;
import com.betfair.video.api.domain.exception.ColdStateException;
import com.betfair.video.api.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.exception.InsufficientFundingException;
import com.betfair.video.api.domain.exception.InvalidInputException;
import com.betfair.video.api.domain.exception.NoSessionException;
import com.betfair.video.api.domain.exception.RestrictedCountryException;
import com.betfair.video.api.domain.exception.StreamHasEndedException;
import com.betfair.video.api.domain.exception.StreamNotFoundException;
import com.betfair.video.api.domain.exception.StreamNotStartedException;
import com.betfair.video.api.domain.exception.UnknownConsumerException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.input.rest.dto.VideoExceptionDto;
import com.betfair.video.api.input.rest.dto.ErrorResponseDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.betfair.video.api.domain.exception.VideoException.ErrorCodeEnum.INVALID_INPUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String X_UUID = "X-UUID";

    @Value("${videoapi.return.stacktrace.on.error}")
    private boolean returnStackTraceOnError;

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({
            StreamNotStartedException.class,
            StreamNotFoundException.class,
            CannotUniquelyResolveStreamException.class,
            StreamHasEndedException.class
    })
    public Map<String, Object> handleNotFoundException(StreamNotStartedException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({
            RestrictedCountryException.class,
            InsufficientFundingException.class,
            BBVInsufficientStakesException.class,
            BBVNoStakesException.class,
    })
    public Map<String, Object> handleForbiddenException(BBVNoStakesException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            Exception.class,
            VideoException.class,
            ErrorInDependentServiceException.class
    })
    public Map<String, Object> handleGeneralException(Exception ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        if (ex instanceof VideoException videoException) {
            logError(uuid, videoException);
            return createErrorResponse(videoException, FaultCode.SERVER);
        }

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

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({NoSessionException.class, InvalidInputException.class, UnknownConsumerException.class})
    public Map<String, Object> handleBadRequestException(VideoException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(InsufficientAccessException.class)
    public Map<String, Object> handleUnauthorizedException(VideoException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.CLIENT);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(ColdStateException.class)
    public Map<String, Object> handleServiceUnavailableException(VideoException ex, HttpServletRequest request) {

        final String uuid = request.getHeader(X_UUID);

        logError(uuid, ex);

        return createErrorResponse(ex, FaultCode.SERVER);
    }

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