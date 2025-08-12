package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;

public interface ReferenceTypesPort {

    ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeId referenceTypeEnum);

}
