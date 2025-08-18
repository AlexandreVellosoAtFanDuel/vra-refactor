package com.betfair.video.api.application.exception;

public class VideoAPIException extends RuntimeException {

    private final ResponseCode responseCode;

    private final String exceptionCode;

    private final VideoAPIExceptionErrorCodeEnum errorCode;
    private final String sportType;

    public VideoAPIException(ResponseCode responseCode, VideoAPIExceptionErrorCodeEnum errorCode, String exceptionCode) {
        this(responseCode, exceptionCode, errorCode, null);
    }

    public VideoAPIException(ResponseCode responseCode, String exceptionCode, VideoAPIExceptionErrorCodeEnum errorCode, String sportType) {
        this.responseCode = responseCode;
        this.exceptionCode = exceptionCode;
        this.errorCode = errorCode;
        this.sportType = sportType;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public VideoAPIExceptionErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getSportType() {
        return sportType;
    }

}
