package com.betfair.video.domain.mapper;

import com.betfair.video.domain.dto.entity.TypeStream;
import com.betfair.video.domain.dto.valueobject.ContentType;

public class TypeStreamMapper {

    private TypeStreamMapper() {
    }

    public static Integer convertContentTypeToStreamTypeId(ContentType contentType) {
        return switch (contentType) {
            case VID -> TypeStream.VID.getId();
            case VIZ -> TypeStream.VIZ.getId();
            case PRE_VID -> TypeStream.PRE_VID.getId();
            default -> null;
        };
    }

}
