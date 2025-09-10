package com.betfair.video.api.infra.input.kafka.dto;

public record BBVRequiredStakes(
        Double defaultThreshold,
        Double sbkThreshold
) {
}
