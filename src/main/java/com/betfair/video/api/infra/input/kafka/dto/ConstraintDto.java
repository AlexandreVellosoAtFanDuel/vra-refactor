package com.betfair.video.api.infra.input.kafka.dto;

import java.util.Set;

public record ConstraintDto(
        Set<String> countries
) {
}
