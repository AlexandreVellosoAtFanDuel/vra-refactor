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
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class VideoStreamInfoDtoMapper {

    public VideoStreamInfoDto mapToDto(VideoStreamInfo videoStreamInfo) {
        return new VideoStreamInfoDto(
                videoStreamInfo.getUniqueVideoId(),
                videoStreamInfo.getProviderId(),
                videoStreamInfo.getBlockedCountries(),
                mapVideoQuality(videoStreamInfo.getVideoQuality()),
                videoStreamInfo.getDefaultBufferInterval(),
                mapSizeRestrictions(videoStreamInfo.getSizeRestrictions()),
                videoStreamInfo.getDirectStream(),
                videoStreamInfo.getInlineStream(),
                mapVideoStreamEndpoint(videoStreamInfo.getVideoStreamEndpoint()),
                videoStreamInfo.getEventId(),
                videoStreamInfo.getEventName(),
                videoStreamInfo.getSportId(),
                videoStreamInfo.getSportName(),
                videoStreamInfo.getProviderEventId(),
                videoStreamInfo.getProviderEventName(),
                videoStreamInfo.getAccountId(),
                videoStreamInfo.getStartDateTime(),
                videoStreamInfo.getCompetition(),
                videoStreamInfo.getDefaultVideoQuality(),
                mapContentType(videoStreamInfo.getContentType())
        );
    }

    private List<VideoQualityDto> mapVideoQuality(List<VideoQuality> videoQuality) {
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

    private SizeRestrictionsDto mapSizeRestrictions(SizeRestrictions sizeRestrictions) {
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

    private ContentTypeDto mapContentType(ContentType contentType) {
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

    private VideoStreamEndpointDto mapVideoStreamEndpoint(VideoStreamEndpoint videoStreamEndpoint) {
        if (videoStreamEndpoint == null) {
            return null;
        }

        PlayerControlParamsDto playerControllParamsDto = new PlayerControlParamsDto(
                "match.lmtPlus",
                "football",
                "61378347"
        );

        return new VideoStreamEndpointDto(
                playerControllParamsDto
        );
    }

}
