package com.betfair.video.api.domain.entity;

import java.util.Date;

public record ScheduleItemData(
        String eventName,
        String venue,
        String country,
        String blockedCountries,
        Date start,
        Date end,
        String competition
) {
}
