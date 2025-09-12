package com.betfair.video.api.input.kafka.mapper;

import com.betfair.video.api.domain.dto.entity.AuditItem;
import com.betfair.video.api.domain.dto.entity.ConfigurationItem;
import com.betfair.video.api.input.kafka.dto.AuditDto;
import com.betfair.video.api.input.kafka.dto.DbConfigDto;

import java.util.Date;

public class ConfigurationItemMapper {

    private ConfigurationItemMapper() {
    }

    public static ConfigurationItem map(DbConfigDto dbConfigDto) {
        if (dbConfigDto == null) {
            return null;
        }

        return new ConfigurationItem(
                dbConfigDto.configType(),
                dbConfigDto.providerId(),
                dbConfigDto.channelType(),
                dbConfigDto.sportType(),
                dbConfigDto.mappingProviderId(),
                dbConfigDto.streamTypeId(),
                dbConfigDto.configValue(),
                dbConfigDto.brandId(),
                mapToAuditItem(dbConfigDto.audit())
        );
    }

    public static AuditItem mapToAuditItem(AuditDto audit) {
        if (audit == null) {
            return null;
        }

        return new AuditItem(
                audit.modifiedBySystemId(),
                audit.modifiedByUser(),
                new Date(audit.modifiedDate()),
                audit.modifiedByIp()
        );
    }

}
