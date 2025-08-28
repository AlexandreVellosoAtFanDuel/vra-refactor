package com.betfair.video.api.application.mapper;

import com.betfair.video.api.application.dto.UserGeolocationDto;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.Geolocation;
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
