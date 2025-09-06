package com.betfair.video.api.domain.dto.valueobject.search;

import com.betfair.video.api.domain.dto.valueobject.ReferenceTypeEnum;

public record ReferenceTypeInfoByIdSearchKey(
        ReferenceTypeEnum referenceTypeId,
        String language
) {
}
