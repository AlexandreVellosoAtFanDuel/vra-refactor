package com.betfair.video.api.domain.port.input;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;

import java.util.List;

public interface RetrieveStreamInfoByExternalIdUseCase {

    VideoStreamInfo retrieveScheduleByExternalId(RequestContext context, String externalIdSource, String externalId,
                                                 Integer channelTypeId, List<Integer> channelSubTypeIds, Integer mobileDeviceId,
                                                 String mobileOsVersion, Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                                 String commentaryLanguage, Integer providerId, ContentTypeDto contentType,
                                                 Boolean includeMetadata, String providerParams);

}
