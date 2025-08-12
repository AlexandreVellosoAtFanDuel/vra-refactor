package com.betfair.video.api.domain.valueobject;

import java.util.Date;
import java.util.List;

public record VideoStreamInfo(
        Long uniqueVideoId,
        Integer providerId,
        String commentaryLanguages,
        String blockedCountries,
        List<String> videoQuality,
        String defaultVideoQuality,
        String defaultBufferInterval,
        SizeRestrictions sizeRestrictions,
        Boolean directStream,
        Boolean inlineStream,
        ContentType contentType,
        VideoStreamEndpoint videoStreamEndpoint,
        String eventId,
        String eventName,
        String sportId,
        String sportName,
        String providerEventId,
        String providerEventName,
        String timeformRaceId,
        Long accountId,
        String exchangeRaceId,
        String videoPlayerConfig,
        Date startDateTime,
        String competition
) {
}
