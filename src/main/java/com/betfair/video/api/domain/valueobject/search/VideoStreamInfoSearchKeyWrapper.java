package com.betfair.video.api.domain.valueobject.search;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;

public record VideoStreamInfoSearchKeyWrapper(
        ExternalIdSource externalIdSource,
        VideoStreamInfoByExternalIdSearchKey videoStreamInfoByExternalIdSearchKey,
        VideoStreamInfoByIdSearchKey videoStreamInfoByIdSearchKey,
        VideoRequestIdentifier videoRequestIdentifier
) {

    public VideoRequestIdentifier getVideoRequestIdentifier(ScheduleItem item) {
        return videoRequestIdentifier;
    }

}
