package com.betfair.video.api.domain.valueobject;

public class VideoStreamInfoDelegate {

    Long accountId;
    private String timeformRaceId;

    private VideoQuality defaultVideoQuality;

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setTimeformRaceId(String timeformRaceId) {
        this.timeformRaceId = timeformRaceId;
    }

    public void setDefaultVideoQuality(VideoQuality defaultVideoQuality) {
        this.defaultVideoQuality = defaultVideoQuality;
    }

    public void setRawDefaultVideoQualityValue(String s) {
        this.defaultVideoQuality = VideoQuality.getValidValues().stream()
                .filter(videoQuality -> videoQuality.name().equalsIgnoreCase(s))
                .findFirst()
                .orElse(VideoQuality.UNRECOGNIZED_VALUE);
    }
}
