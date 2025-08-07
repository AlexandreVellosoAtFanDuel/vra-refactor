package com.betfair.video.api.application.exception;

public class VideoAPIException extends RuntimeException {

    private final ResponseCode responseCode;
    private final VideoAPIExceptionErrorCodeEnum errorCode;
    private final String sportType;

    public VideoAPIException(ResponseCode responseCode, VideoAPIExceptionErrorCodeEnum errorCode) {
        this(responseCode, errorCode, null);
    }

    public VideoAPIException(ResponseCode responseCode, VideoAPIExceptionErrorCodeEnum errorCode, String sportType) {
        this.responseCode = responseCode;
        this.errorCode = errorCode;
        this.sportType = sportType;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public VideoAPIExceptionErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getSportType() {
        return sportType;
    }

}
