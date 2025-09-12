package com.betfair.video.domain.port.input;

import com.betfair.video.domain.dto.entity.RequestContext;
import com.betfair.video.domain.dto.valueobject.ContentType;
import com.betfair.video.domain.dto.valueobject.VideoQuality;
import com.betfair.video.domain.dto.valueobject.VideoStreamInfo;

import java.util.List;

public interface EventServicePort {

    VideoStreamInfo retrieveScheduleByExternalId(RequestContext context, String externalIdSource, String externalId,
                                                 Integer channelTypeId, List<Integer> channelSubTypeIds, Integer mobileDeviceId,
                                                 String mobileOsVersion, Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                                 String commentaryLanguage, Integer providerId, ContentType contentType,
                                                 Boolean includeMetadata, String providerParams);

}
