package com.betfair.video.domain.dto.search;

import com.betfair.video.domain.dto.valueobject.ReferenceTypeEnum;

public record ReferenceTypeInfoByIdSearchKey(
        ReferenceTypeEnum referenceTypeId,
        String language
) {
}
