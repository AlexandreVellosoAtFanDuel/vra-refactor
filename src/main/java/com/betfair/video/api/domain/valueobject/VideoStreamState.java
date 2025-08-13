package com.betfair.video.api.domain.valueobject;

public enum VideoStreamState {

    /**
     * Requested video stream is not started yet
     */
    NOT_STARTED("NOT_STARTED"),
    /**
     * Requested stream is currently active
     */
    PRE_STREAM("PRE_STREAM"),
    /**
     * Requested stream is currently not active
     */
    STREAM("STREAM"),
    /**
     * Requested stream has already finished
     */
    FINISHED("FINISHED"),
    /**
     * Error occurred requesting stream
     */
    ERROR("ERROR"),
    UNRECOGNIZED_VALUE(null);

    private final String value;

    VideoStreamState(String value) {
        this.value = value;
    }
}
