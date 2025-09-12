package com.betfair.video.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        String videoPlayerConfig,
        Date startDateTime,
        String competition,
        String defaultVideoQuality,
        ContentTypeDto contentType
) {
}
