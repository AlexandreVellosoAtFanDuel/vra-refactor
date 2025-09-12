package com.betfair.video.api.input.kafka.dto;

public record BBVRequiredStakes(
        Double defaultThreshold,
        Double sbkThreshold
) {
}
