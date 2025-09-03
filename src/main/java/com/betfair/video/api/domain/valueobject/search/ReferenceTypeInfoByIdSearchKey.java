package com.betfair.video.api.domain.valueobject.search;

import com.betfair.video.api.domain.valueobject.ReferenceTypeEnum;

public record ReferenceTypeInfoByIdSearchKey(
        ReferenceTypeEnum referenceTypeId,
        String language
) {
}
