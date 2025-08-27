package com.betfair.video.api.application.dto;

import java.util.Date;
import java.util.List;

public record VideoStreamInfoDto(
        Long uniqueVideoId,
        Integer providerId,
        String blockedCountries,
        List<VideoQualityDto> videoQuality,
        String defaultBufferInterval,
        SizeRestrictionsDto sizeRestrictions,
        Boolean directStream,
        Boolean inlineStream,
        VideoStreamEndpointDto videoStreamEndpoint,
        String eventId,
        String eventName,
        String sportId,
        String sportName,
        String providerEventId,
        String providerEventName,
        Long accountId,
        Date startDateTime,
        String competition,
        String defaultVideoQuality,
        ContentTypeDto contentType
) {
}
