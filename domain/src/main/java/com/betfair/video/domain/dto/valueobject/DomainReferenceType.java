package com.betfair.video.domain.dto.valueobject;

import java.io.Serializable;

public record DomainReferenceType(
        String type,
        Integer id,
        String description
) implements Serializable {
}
