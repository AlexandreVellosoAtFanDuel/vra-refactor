package com.betfair.video.api.input.kafka.dto;

import java.util.Set;

public record ConstraintDto(
        Set<String> countries
) {
}
