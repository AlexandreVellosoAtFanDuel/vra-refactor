package com.betfair.video.input.kafka.dto;

import java.util.Set;

public record ConstraintDto(
        Set<String> countries
) {
}
