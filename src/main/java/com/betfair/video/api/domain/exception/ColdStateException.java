package com.betfair.video.api.domain.exception;

public class ColdStateException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.COLD_STATE;

    private static final String DEFAULT_MESSAGE = "Cold state";

    public ColdStateException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public ColdStateException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public ColdStateException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
