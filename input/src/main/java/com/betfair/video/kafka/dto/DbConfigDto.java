package com.betfair.video.kafka.dto;

public record DbConfigDto(
        Integer id,
        String configType,
        Integer providerId,
        Integer channelType,
        Integer sportType,
        Integer mappingProviderId,
        Integer streamTypeId,
        Integer brandId,
        String configValue,
        AuditDto audit,
        Integer factoryId,
        String identifier,
        String entityName
) {
}
