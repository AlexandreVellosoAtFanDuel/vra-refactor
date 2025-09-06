package com.betfair.video.api.domain.dto.valueobject.search;

import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;

import java.util.List;
import java.util.Set;

public class VideoStreamInfoByIdSearchKey extends VRAStreamSearchKey {

    private Long uniqueVideoId;

    public VideoStreamInfoByIdSearchKey(Integer channelTypeId, List<Integer> channelSubTypeIds,
                                       Integer mobileDeviceId, String mobileOsVersion,
                                       Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                       String commentaryLanguage, Integer providerId,
                                       ContentTypeDto contentType, Set<Integer> streamTypeIds,
                                       Set<Integer> brandIds, String providerParams,
                                       Long uniqueVideoId) {
        super(channelTypeId, channelSubTypeIds, mobileDeviceId, mobileOsVersion, mobileScreenDensityDpi,
              videoQuality, commentaryLanguage, providerId, contentType, streamTypeIds, brandIds, providerParams);
        this.uniqueVideoId = uniqueVideoId;
    }

    public Long getUniqueVideoId() {
        return uniqueVideoId;
    }

    public void setUniqueVideoId(Long uniqueVideoId) {
        this.uniqueVideoId = uniqueVideoId;
    }
}
