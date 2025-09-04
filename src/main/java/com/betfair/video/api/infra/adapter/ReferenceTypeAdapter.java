package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ReferenceType;
import com.betfair.video.api.domain.port.ReferenceTypePort;
import com.betfair.video.api.domain.valueobject.DomainReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeEnum;
import com.betfair.video.api.domain.valueobject.search.ReferenceTypeInfoByIdSearchKey;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReferenceTypeAdapter implements ReferenceTypePort, RefreshCache<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceTypeAdapter.class);

    private final IMap<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> referenceTypesMap;

    public ReferenceTypeAdapter(IMap<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> referenceTypesMap) {
        this.referenceTypesMap = referenceTypesMap;
    }

    @Override
    public ReferenceType findReferenceTypeById(Integer referenceTypeId, ReferenceTypeEnum referenceTypeEnum) {
        // TODO: Implement search here based on cache
        return null;
    }

    @Override
    public void insertItemsToCache(Map<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> newItems) {
        if (referenceTypesMap.isEmpty()) {
            logger.info("Revalidating reference types cache with {} items", newItems.size());
            referenceTypesMap.putAll(newItems);
        }
    }

}
