package com.betfair.video.api.infra.input.rest.mapper;

import com.betfair.video.api.domain.dto.valueobject.SizeRestrictions;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.infra.input.rest.dto.SizeRestrictionsDto;
import com.betfair.video.api.infra.input.rest.dto.VideoQualityDto;
import com.betfair.video.api.infra.input.rest.dto.VideoStreamInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface VideoStreamInfoDtoMapper {

    @Mapping(target = "videoQuality", source = "videoQuality", qualifiedByName = "mapVideoQualityList")
    @Mapping(target = "sizeRestrictions", source = "sizeRestrictions", qualifiedByName = "mapSizeRestrictions")
    @Mapping(target = "videoStreamEndpoint", source = "videoStreamEndpoint")
    @Mapping(target = "contentType", source = "contentType", qualifiedByName = "mapContentType")
    VideoStreamInfoDto mapToDto(VideoStreamInfo videoStreamInfo);

    @Named("mapVideoQualityList")
    default List<VideoQualityDto> mapVideoQualityList(List<VideoQuality> videoQuality) {
        if (videoQuality == null) {
            return Collections.emptyList();
        }

        return videoQuality.stream().map(quality -> switch (quality) {
            case VERY_LOW -> VideoQualityDto.VERY_LOW;
            case LOW -> VideoQualityDto.LOW;
            case MEDIUM -> VideoQualityDto.MEDIUM;
            case HIGH -> VideoQualityDto.HIGH;
            default -> VideoQualityDto.UNRECOGNIZED_VALUE;
        }).toList();
    }

    @Named("mapSizeRestrictions")
    default SizeRestrictionsDto mapSizeRestrictions(SizeRestrictions sizeRestrictions) {
        if (sizeRestrictions == null) {
            return null;
        }

        return new SizeRestrictionsDto(
                sizeRestrictions.fullScreenAllowed(),
                sizeRestrictions.airPlayAllowed(),
                sizeRestrictions.aspectRatio(),
                sizeRestrictions.widthMax(),
                sizeRestrictions.widthDefault()
        );
    }

    @Named("mapContentType")
    default ContentTypeDto mapContentType(com.betfair.video.api.domain.dto.valueobject.ContentType contentType) {
        if (contentType == null) {
            return null;
        }

        return switch (contentType) {
            case VID -> ContentTypeDto.VID;
            case VIZ -> ContentTypeDto.VIZ;
            case PRE_VID -> ContentTypeDto.PRE_VID;
            default -> ContentTypeDto.UNRECOGNIZED_VALUE;
        };
    }

}
