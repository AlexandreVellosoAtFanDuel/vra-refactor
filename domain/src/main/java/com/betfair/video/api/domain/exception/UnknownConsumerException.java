package com.betfair.video.api.domain.exception;

public class UnknownConsumerException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.UNKNOWN_CONSUMER;
    private static final String DEFAULT_MESSAGE = "Unknown Consumer";

    public UnknownConsumerException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public UnknownConsumerException(String message) {
        super(ERROR_CODE, message, null);
    }
}
