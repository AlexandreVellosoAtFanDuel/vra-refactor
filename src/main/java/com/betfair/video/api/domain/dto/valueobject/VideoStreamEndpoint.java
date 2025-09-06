package com.betfair.video.api.domain.dto.valueobject;

import java.util.Map;

public class VideoStreamEndpoint {
        private Integer videoFormat;
        private String videoQuality;
        private String videoEndpoint;
        private Map<String,String> playerControlParams;

    public Integer getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(Integer videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getVideoEndpoint() {
        return videoEndpoint;
    }

    public void setVideoEndpoint(String videoEndpoint) {
        this.videoEndpoint = videoEndpoint;
    }

    public Map<String, String> getPlayerControlParams() {
        return playerControlParams;
    }

    public void setPlayerControlParams(Map<String, String> playerControlParams) {
        this.playerControlParams = playerControlParams;
    }
}
