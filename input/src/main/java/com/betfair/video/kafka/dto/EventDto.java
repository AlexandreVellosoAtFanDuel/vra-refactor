package com.betfair.video.kafka.dto;


import java.util.List;

public record EventDto(
    String eventId,
    Integer sportId,
    Integer providerId,
    String country,
    String meetingName,
    String venue,
    String eventDescription,
    Long eventTime,
    List<StreamInfoDto> streamInfo,
    List<RaceDto> races,
    ConstraintDto constraints
) {
}
