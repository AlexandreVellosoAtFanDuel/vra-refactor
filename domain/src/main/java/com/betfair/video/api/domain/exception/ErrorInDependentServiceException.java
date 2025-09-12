package com.betfair.video.api.domain.exception;

public class ErrorInDependentServiceException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE;
    private static final String DEFAULT_MESSAGE = "Error in dependent service";

    public ErrorInDependentServiceException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public ErrorInDependentServiceException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public ErrorInDependentServiceException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
