package com.betfair.video.api.domain.port.output;

import com.betfair.video.api.domain.dto.entity.ReferenceType;
import com.betfair.video.api.domain.dto.valueobject.ReferenceTypeEnum;

public interface ReferenceTypePort {

    ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeEnum referenceTypeEnum);

}
