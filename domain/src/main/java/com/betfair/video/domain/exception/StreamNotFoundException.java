package com.betfair.video.domain.exception;

public class StreamNotFoundException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.STREAM_NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "Stream not found";

    public StreamNotFoundException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public StreamNotFoundException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public StreamNotFoundException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
