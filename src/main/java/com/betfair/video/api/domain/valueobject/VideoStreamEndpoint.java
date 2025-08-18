package com.betfair.video.api.domain.valueobject;

import java.util.Map;

public class VideoStreamEndpoint {
        private Integer videoFormat;
        private String videoQuality;
        private String videoEndpoint;
        private Map<StreamDetailsParamEnum,String> playerControlParams;

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public void setVideoEndpoint(String videoEndpoint) {
        this.videoEndpoint = videoEndpoint;
    }

    public void setPlayerControlParams(Map<StreamDetailsParamEnum,String> playerControlParams) {
        this.playerControlParams = playerControlParams;
    }
}
