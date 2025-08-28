package com.betfair.video.api.domain.entity;

import java.util.Date;

public record AuditItem(
        Integer modifiedBySystemId,
        String modifiedByUser,
        Date modifiedDate,
        String modifiedByIp
) {
}
