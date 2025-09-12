package com.betfair.video.domain.exception;

public class InsufficientAccessException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.INSUFFICIENT_ACCESS;

    private static final String DEFAULT_MESSAGE = "Access permissions are insufficient for the requested operation or data for user";

    public InsufficientAccessException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public InsufficientAccessException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public InsufficientAccessException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
