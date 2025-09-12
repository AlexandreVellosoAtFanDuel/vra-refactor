package com.betfair.video.domain.exception;

public class StreamNotStartedException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.STREAM_NOT_STARTED;
    private static final String DEFAULT_MESSAGE = "Stream has not started yet";

    public StreamNotStartedException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public StreamNotStartedException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public StreamNotStartedException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
