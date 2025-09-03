package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.entity.ReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeEnum;

public interface ReferenceTypePort {

    ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeEnum referenceTypeEnum);

}
