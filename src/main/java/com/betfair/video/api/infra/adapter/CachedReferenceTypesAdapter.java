package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.port.ReferenceTypesPort;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeId;
import org.springframework.stereotype.Component;

@Component
public class CachedReferenceTypesAdapter implements ReferenceTypesPort {

    @Override
    public ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeId referenceTypeEnum) {
        // TODO: Implement actual caching and fetching logic here
        return new ReferenceType("-1", "Other sports types");
    }

}
