package com.betfair.video.api.input.rest.mapper;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.RetrieveScheduleByExternalIdParams;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.input.rest.dto.RetrieveStreamInfoByExternalIdDto;

public class RetrieveStreamInfoByExternalIdMapper {

    private RetrieveStreamInfoByExternalIdMapper() {
    }

    public static RetrieveScheduleByExternalIdParams map(RequestContext context, RetrieveStreamInfoByExternalIdDto params) {
        VideoQuality videoQuality = null;
        ContentType contentType = null;

        if (params.videoQuality() != null) {
            videoQuality = VideoQuality.fromValue(params.videoQuality().getValue());
        }

        if (params.contentType() != null) {
            contentType = ContentType.fromValue(params.contentType().getValue());
        }

        return new RetrieveScheduleByExternalIdParams(
                context,
                params.externalIdSource(),
                params.externalId(),
                params.channelTypeId(),
                params.channelSubTypeIds(),
                params.mobileDeviceId(),
                params.mobileOsVersion(),
                params.mobileScreenDensityDpi(),
                videoQuality,
                params.commentaryLanguage(),
                params.providerId(),
                contentType,
                params.includeMetadata(),
                params.providerParams()
        );
    }

}
