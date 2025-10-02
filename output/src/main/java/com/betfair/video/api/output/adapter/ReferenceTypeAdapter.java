package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.ReferenceType;
import com.betfair.video.api.domain.dto.search.ReferenceTypeInfoByIdSearchKey;
import com.betfair.video.api.domain.dto.valueobject.DomainReferenceType;
import com.betfair.video.api.domain.dto.valueobject.ReferenceTypeEnum;
import com.betfair.video.api.domain.exception.ColdStateException;
import com.betfair.video.api.domain.port.output.ReferenceTypePort;
import com.betfair.video.api.domain.port.output.RefreshMapCache;
import com.betfair.video.api.output.dto.ReferenceTypeInfo;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        if (referenceTypeId == null) {
            return null;
        }

        ReferenceTypeInfo referenceTypeInfo = this.getReferenceTypeById(new ReferenceTypeInfoByIdSearchKey(referenceTypeEnum, null));
        if (referenceTypeInfo == null) {
            return null;
        }

        for (ReferenceType referenceType : referenceTypeInfo.referenceTypes()) {
            if (String.valueOf(referenceTypeId).equals(referenceType.referenceTypeId())) {
                return referenceType;
            }
        }

        return null;
    }

    public ReferenceTypeInfo getReferenceTypeById(ReferenceTypeInfoByIdSearchKey searchKey) {
        if (CollectionUtils.isEmpty(this.referenceTypesMap)) {
            throw new ColdStateException("Reference types cache is in cold state. Probably VRA didn't receive schedule message or it was empty.");
        }

        List<ReferenceType> referenceTypes = this.referenceTypesMap.get(searchKey)
                .stream()
                .map(type -> new ReferenceType(String.valueOf(type.id()), type.description()))
                .toList();

        return new ReferenceTypeInfo(searchKey.referenceTypeId(), referenceTypes);
    }

    @Override
    public void insertItemsToCache(Map<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> newItems) {
        if (referenceTypesMap.isEmpty()) {
            logger.info("Revalidating reference types cache with {} items", newItems.size());
            referenceTypesMap.putAll(newItems);
        }
    }

}
