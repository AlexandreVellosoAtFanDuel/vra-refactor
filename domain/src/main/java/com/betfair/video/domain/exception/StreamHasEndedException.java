package com.betfair.video.domain.exception;

public class StreamHasEndedException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.STREAM_HAS_ENDED;

    private static final String DEFAULT_MESSAGE = "Stream has ended";

    public StreamHasEndedException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public StreamHasEndedException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public StreamHasEndedException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
