package com.betfair.video.api.infra.input.kafka.mapper;

import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.valueobject.search.ConfigurationSearchKey;
import com.betfair.video.api.infra.input.kafka.dto.DbConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ConfigurationSearchKeyMapper {

    @Mapping(target = "configType", source = "configType", qualifiedByName = "mapConfigurationType")
    @Mapping(target = "providerId", source = "providerId")
    @Mapping(target = "channelType", source = "channelType")
    @Mapping(target = "sportType", source = "sportType")
    @Mapping(target = "mappingProviderId", source = "mappingProviderId")
    @Mapping(target = "ignoreSportType", constant = "false")
    @Mapping(target = "streamType", source = "streamTypeId")
    @Mapping(target = "brandId", source = "brandId")
    ConfigurationSearchKey map(DbConfigDto dbConfigDto);

    @Named("mapConfigurationType")
    default ConfigurationType mapConfigurationType(String configType) {
        return ConfigurationType.fromString(configType);
    }

}
