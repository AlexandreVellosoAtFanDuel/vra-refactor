package com.betfair.video.kafka.dto;

import java.util.List;

public record RaceDto(
        String raceId,
        Long raceTime,
        String winMarketName,
        List<StreamInfoDto> streamInfo,
        ConstraintDto constraints
) {
}
