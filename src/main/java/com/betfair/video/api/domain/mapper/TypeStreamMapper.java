package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.valueobject.ContentType;

public class TypeStreamMapper {

    public Integer convertContentTypeToStreamTypeId(ContentType contentType) {
        return switch (contentType) {
            case VID -> TypeStream.VID.getId();
            case VIZ -> TypeStream.VIZ.getId();
            case PRE_VID -> TypeStream.PRE_VID.getId();
            default -> null;
        };
    }

}
