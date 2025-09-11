package com.betfair.video.api.infra.input.kafka.mapper;

import com.betfair.video.api.domain.dto.entity.ConfigurationType;
import com.betfair.video.api.domain.dto.search.ConfigurationSearchKey;
import com.betfair.video.api.infra.input.kafka.dto.DbConfigDto;

public class ConfigurationSearchKeyMapper {

    private ConfigurationSearchKeyMapper() {
    }

    public static ConfigurationSearchKey map(DbConfigDto dbConfigDto) {
        if (dbConfigDto == null) {
            return null;
        }

        return new ConfigurationSearchKey(
                mapConfigurationType(dbConfigDto.configType()),
                dbConfigDto.providerId(),
                dbConfigDto.channelType(),
                dbConfigDto.sportType(),
                dbConfigDto.mappingProviderId(),
                false,
                dbConfigDto.streamTypeId(),
                dbConfigDto.brandId()
        );
    }

    private static ConfigurationType mapConfigurationType(String configType) {
        return ConfigurationType.fromString(configType);
    }

}
