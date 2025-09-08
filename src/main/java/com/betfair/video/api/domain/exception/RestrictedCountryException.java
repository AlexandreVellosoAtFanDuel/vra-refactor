package com.betfair.video.api.domain.exception;

public class RestrictedCountryException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.RESTRICTED_COUNTRY;
    private static final String DEFAULT_MESSAGE = "Restricted Country";

    public RestrictedCountryException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public RestrictedCountryException(String message) {
        super(ERROR_CODE, message, null);
    }
}
