package com.betfair.video.api.output.dto;

import com.betfair.video.api.domain.dto.entity.ReferenceType;
import com.betfair.video.api.domain.dto.valueobject.ReferenceTypeEnum;

import java.util.List;

public record ReferenceTypeInfo(
        ReferenceTypeEnum typeId,
        List<ReferenceType> referenceTypes
) {
}
