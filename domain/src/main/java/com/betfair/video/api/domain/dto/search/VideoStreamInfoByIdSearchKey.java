package com.betfair.video.api.domain.dto.search;

import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;

import java.util.List;
import java.util.Set;

public class VideoStreamInfoByIdSearchKey extends VRAStreamSearchKey {

    private final Long uniqueVideoId;

    public VideoStreamInfoByIdSearchKey(Integer channelTypeId, List<Integer> channelSubTypeIds,
                                        Integer mobileDeviceId, String mobileOsVersion,
                                        Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                        String commentaryLanguage, Integer providerId,
                                        ContentType contentType, Set<Integer> streamTypeIds,
                                        Set<Integer> brandIds, String providerParams,
                                        Long uniqueVideoId) {
        super(channelTypeId, channelSubTypeIds, mobileDeviceId, mobileOsVersion, mobileScreenDensityDpi,
              videoQuality, commentaryLanguage, providerId, contentType, streamTypeIds, brandIds, providerParams);
        this.uniqueVideoId = uniqueVideoId;
    }

    public Long getUniqueVideoId() {
        return uniqueVideoId;
    }

}
