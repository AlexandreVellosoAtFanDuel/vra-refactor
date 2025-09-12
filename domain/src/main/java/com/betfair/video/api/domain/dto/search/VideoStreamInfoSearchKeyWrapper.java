package com.betfair.video.api.domain.dto.search;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;

public class VideoStreamInfoSearchKeyWrapper {
    private ExternalIdSource externalIdSource;
    private VideoStreamInfoByExternalIdSearchKey videoStreamInfoByExternalIdSearchKey;
    private VideoStreamInfoByIdSearchKey videoStreamInfoByIdSearchKey;
    private VideoRequestIdentifier videoRequestIdentifier;

    public VideoStreamInfoSearchKeyWrapper(ExternalIdSource externalIdSource,
                                           VideoStreamInfoByExternalIdSearchKey videoStreamInfoByExternalIdSearchKey,
                                           VideoStreamInfoByIdSearchKey videoStreamInfoByIdSearchKey,
                                           VideoRequestIdentifier videoRequestIdentifier) {
        this.externalIdSource = externalIdSource;
        this.videoStreamInfoByExternalIdSearchKey = videoStreamInfoByExternalIdSearchKey;
        this.videoStreamInfoByIdSearchKey = videoStreamInfoByIdSearchKey;
        this.videoRequestIdentifier = videoRequestIdentifier;
    }

    public VideoRequestIdentifier getVideoRequestIdentifier(ScheduleItem item) {
        return new VideoRequestIdentifier(null, null, null, null, null, null);
    }

    public VideoStreamInfoByExternalIdSearchKey getVideoStreamInfoByExternalIdSearchKey() {
        return videoStreamInfoByExternalIdSearchKey;
    }

    public ExternalIdSource getExternalIdSource() {
        return externalIdSource;
    }

    public void setVideoRequestIdentifier(VideoRequestIdentifier eventIdentifier) {
        this.videoRequestIdentifier = eventIdentifier;
    }
}
