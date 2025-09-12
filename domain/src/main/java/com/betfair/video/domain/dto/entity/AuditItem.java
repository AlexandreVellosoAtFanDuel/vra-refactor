package com.betfair.video.domain.dto.entity;

import java.io.Serializable;
import java.util.Date;

public record AuditItem(
        Integer modifiedBySystemId,
        String modifiedByUser,
        Date modifiedDate,
        String modifiedByIp
) implements Serializable {
}
