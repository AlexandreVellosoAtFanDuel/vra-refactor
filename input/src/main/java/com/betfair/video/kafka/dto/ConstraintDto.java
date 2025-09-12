package com.betfair.video.kafka.dto;

import java.util.Set;

public record ConstraintDto(
        Set<String> countries
) {
}
