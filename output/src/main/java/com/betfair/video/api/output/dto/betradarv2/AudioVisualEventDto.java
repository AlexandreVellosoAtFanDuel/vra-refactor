package com.betfair.video.api.output.dto.betradarv2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AudioVisualEventDto(
        String id,
        @JsonProperty("sport_event_id")
        String sportEventId,
        @JsonProperty("start_time")
        String startTime,
        @JsonProperty("end_time")
        String endTime,
        @JsonProperty("event_status")
        BaseDto eventStatus,
        @JsonProperty("first_level_category")
        BaseDto firstLevelCategory,
        @JsonProperty("second_level_category")
        BaseDto secondLevelCategory,
        @JsonProperty("third_level_category")
        BaseDto thirdLevelCategory,
        List<CompetitorDto> competitors,
        BaseDto venue,
        List<ContentDto> contents
) {
}
