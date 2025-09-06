package com.betfair.video.api.domain.exception;

public class StreamNotStartedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Stream has not started yet. ";

    private final String sportType;

    public StreamNotStartedException() {
        super(DEFAULT_MESSAGE);
        this.sportType = null;
    }

    public StreamNotStartedException(String sportType) {
        super(DEFAULT_MESSAGE);
        this.sportType = sportType;
    }

    public String getSportType() {
        return sportType;
    }
}
