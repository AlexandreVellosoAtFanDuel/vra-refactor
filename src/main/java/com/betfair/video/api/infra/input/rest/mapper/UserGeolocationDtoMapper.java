package com.betfair.video.api.infra.input.rest.mapper;

import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.infra.input.rest.dto.UserGeolocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserGeolocationDtoMapper {

    @Mapping(target = "countryCode", source = "geolocation.countryCode")
    @Mapping(target = "subDivisionCode", source = "geolocation.subDivisionCode")
    @Mapping(target = "dmaId", source = "geolocation.dmaId")
    UserGeolocationDto mapToDto(User user);


    UserGeolocationDto mapFromGeolocation(Geolocation geolocation);

}
