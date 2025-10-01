package com.betfair.video.api.domain.exception;

public class NoSessionException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.NO_SESSION;

    private static final String DEFAULT_MESSAGE = "No session provided";

    public NoSessionException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public NoSessionException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public NoSessionException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }
}
