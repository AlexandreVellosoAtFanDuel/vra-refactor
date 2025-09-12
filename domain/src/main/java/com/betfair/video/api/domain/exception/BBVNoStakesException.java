package com.betfair.video.api.domain.exception;

public class BBVNoStakesException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.BBV_NO_STAKES;
    private static final String DEFAULT_MESSAGE = "Bet before view - no stakes available";

    public BBVNoStakesException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public BBVNoStakesException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public BBVNoStakesException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
