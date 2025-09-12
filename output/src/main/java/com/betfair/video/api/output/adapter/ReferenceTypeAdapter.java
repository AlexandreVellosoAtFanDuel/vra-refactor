package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.ReferenceType;
import com.betfair.video.api.domain.dto.search.ReferenceTypeInfoByIdSearchKey;
import com.betfair.video.api.domain.dto.valueobject.DomainReferenceType;
import com.betfair.video.api.domain.dto.valueobject.ReferenceTypeEnum;
import com.betfair.video.api.domain.port.output.ReferenceTypePort;
import com.betfair.video.api.domain.port.output.RefreshMapCache;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReferenceTypeAdapter implements ReferenceTypePort, RefreshMapCache<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> {

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
