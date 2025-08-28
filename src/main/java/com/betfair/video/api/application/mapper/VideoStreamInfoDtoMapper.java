package com.betfair.video.api.application.mapper;

import com.betfair.video.api.application.dto.ContentTypeDto;
import com.betfair.video.api.application.dto.PlayerControlParamsDto;
import com.betfair.video.api.application.dto.SizeRestrictionsDto;
import com.betfair.video.api.application.dto.VideoQualityDto;
import com.betfair.video.api.application.dto.VideoStreamEndpointDto;
import com.betfair.video.api.application.dto.VideoStreamInfoDto;
import com.betfair.video.api.domain.valueobject.ContentType;
import com.betfair.video.api.domain.valueobject.SizeRestrictions;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.VideoStreamEndpoint;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
@Component
public interface VideoStreamInfoDtoMapper {

    @Mapping(target = "videoQuality", source = "videoQuality", qualifiedByName = "mapVideoQualityList")
    @Mapping(target = "sizeRestrictions", source = "sizeRestrictions", qualifiedByName = "mapSizeRestrictions")
    @Mapping(target = "videoStreamEndpoint", source = "videoStreamEndpoint", qualifiedByName = "mapVideoStreamEndpoint")
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
    default ContentTypeDto mapContentType(ContentType contentType) {
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

    @Named("mapVideoStreamEndpoint")
    default VideoStreamEndpointDto mapVideoStreamEndpoint(VideoStreamEndpoint videoStreamEndpoint) {
        if (videoStreamEndpoint == null) {
            return null;
        }

        Map<String, String> playerControlParams = Map.of("streamFormat", "hls");

        return new VideoStreamEndpointDto(
                "https://api.livestreaming.imgarena.com/api/v2/streaming/events2/stream?operatorId=226",
                playerControlParams
        );
    }
}
