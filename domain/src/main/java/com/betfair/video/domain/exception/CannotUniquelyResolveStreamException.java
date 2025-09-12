package com.betfair.video.domain.exception;

public class CannotUniquelyResolveStreamException extends VideoException {

    private static final ErrorCodeEnum ERROR_CODE = ErrorCodeEnum.CANNOT_UNIQUELY_RESOLVE_STREAM;

    private static final String DEFAULT_MESSAGE = "Cannot uniquely resolve stream";

    public CannotUniquelyResolveStreamException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, null);
    }

    public CannotUniquelyResolveStreamException(String sportType) {
        super(ERROR_CODE, DEFAULT_MESSAGE, sportType);
    }

    public CannotUniquelyResolveStreamException(String message, String sportType) {
        super(ERROR_CODE, message, sportType);
    }

}
