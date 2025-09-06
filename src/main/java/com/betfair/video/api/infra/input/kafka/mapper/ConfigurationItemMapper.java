package com.betfair.video.api.infra.input.kafka.mapper;

import com.betfair.video.api.domain.dto.entity.AuditItem;
import com.betfair.video.api.domain.dto.entity.ConfigurationItem;
import com.betfair.video.api.infra.input.kafka.dto.AuditDto;
import com.betfair.video.api.infra.input.kafka.dto.DbConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Date;

@Mapper(componentModel = "spring")
@Component
public interface ConfigurationItemMapper {

    @Mapping(target = "configType", source = "configType")
    @Mapping(target = "providerId", source = "providerId")
    @Mapping(target = "channelType", source = "channelType")
    @Mapping(target = "sportType", source = "sportType")
    @Mapping(target = "mappingProviderId", source = "mappingProviderId")
    @Mapping(target = "streamTypeId", source = "streamTypeId")
    @Mapping(target = "configValue", source = "configValue")
    @Mapping(target = "brandId", source = "brandId")
    @Mapping(target = "audit", source = "audit", qualifiedByName = "mapToAuditItem")
    ConfigurationItem map(DbConfigDto dbConfigDto);

    @Named("mapToAuditItem")
    default AuditItem mapToAuditItem(AuditDto audit) {
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
