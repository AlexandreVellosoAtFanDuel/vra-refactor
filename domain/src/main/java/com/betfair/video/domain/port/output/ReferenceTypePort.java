package com.betfair.video.domain.port.output;

import com.betfair.video.domain.dto.entity.ReferenceType;
import com.betfair.video.domain.dto.valueobject.ReferenceTypeEnum;

public interface ReferenceTypePort {

    ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeEnum referenceTypeEnum);

}
