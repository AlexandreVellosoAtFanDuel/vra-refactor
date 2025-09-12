package com.betfair.video.api.domain.exception;

public class InsufficientFundingException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.INSUFFICIENT_FUNDING;

    private static final String DEFAULT_MESSAGE = "Insufficient funding to place bet";

    public InsufficientFundingException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public InsufficientFundingException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public InsufficientFundingException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
