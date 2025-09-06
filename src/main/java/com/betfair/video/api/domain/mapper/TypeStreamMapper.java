package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.domain.entity.TypeStream;
import org.springframework.stereotype.Component;

@Component
public class TypeStreamMapper {

    public Integer convertContentTypeToStreamTypeId(ContentTypeDto contentType) {
        return switch (contentType) {
            case VID -> TypeStream.VID.getId();
            case VIZ -> TypeStream.VIZ.getId();
            case PRE_VID -> TypeStream.PRE_VID.getId();
            default -> null;
        };
    }

}
