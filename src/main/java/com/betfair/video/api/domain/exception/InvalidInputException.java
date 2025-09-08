package com.betfair.video.api.domain.exception;

public class InvalidInputException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.INVALID_INPUT;

    private static final String DEFAULT_MESSAGE = "Invalid input provided";

    public InvalidInputException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public InvalidInputException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public InvalidInputException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
