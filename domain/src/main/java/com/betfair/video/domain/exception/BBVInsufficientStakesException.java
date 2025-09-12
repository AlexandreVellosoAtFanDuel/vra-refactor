package com.betfair.video.domain.exception;

public class BBVInsufficientStakesException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.BBV_INSUFFICIENT_STAKES;
    private static final String DEFAULT_MESSAGE = "Bet before view - insufficient stakes";

    public BBVInsufficientStakesException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public BBVInsufficientStakesException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public BBVInsufficientStakesException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
