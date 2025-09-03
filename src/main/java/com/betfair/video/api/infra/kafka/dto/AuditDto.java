package com.betfair.video.api.infra.kafka.dto;

public record AuditDto(
        Integer id,
        Integer modifiedBySystemId,
        String modifiedByUser,
        Long modifiedDate,
        String modifiedByIp,
        Integer factoryId
) {
}
