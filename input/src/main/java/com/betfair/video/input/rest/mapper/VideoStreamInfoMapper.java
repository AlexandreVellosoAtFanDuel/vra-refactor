package com.betfair.video.input.rest.mapper;

import com.betfair.video.domain.dto.valueobject.ContentType;
import com.betfair.video.domain.dto.valueobject.SizeRestrictions;
import com.betfair.video.domain.dto.valueobject.VideoQuality;
import com.betfair.video.domain.dto.valueobject.VideoStreamEndpoint;
import com.betfair.video.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.input.rest.dto.VideoQualityDto;
import com.betfair.video.input.rest.dto.VideoStreamInfoDto;
import com.betfair.video.input.rest.dto.ContentTypeDto;
import com.betfair.video.input.rest.dto.SizeRestrictionsDto;
import com.betfair.video.input.rest.dto.VideoStreamEndpointDto;

import java.util.List;

public class VideoStreamInfoMapper {

    private VideoStreamInfoMapper() {
    }

    public static VideoStreamInfoDto mapToDto(VideoStreamInfo videoStreamInfo) {
        if (videoStreamInfo == null) {
            return null;
        }

        List<VideoQualityDto> videoQualityDTOs = videoStreamInfo.videoQuality().stream()
                .map(VideoStreamInfoMapper::mapVideoQualityToDto)
                .toList();

        SizeRestrictionsDto sizeRestrictions = mapSizeRestrictionsToDto(videoStreamInfo.sizeRestrictions());

        VideoStreamEndpointDto videoStreamEndpoint = mapVideoStreamEndpointToDto(videoStreamInfo.videoStreamEndpoint());

        ContentTypeDto contentType = mapContentTypeToDto(videoStreamInfo.contentType());

        return new VideoStreamInfoDto(
                videoStreamInfo.uniqueVideoId(),
                videoStreamInfo.providerId(),
                videoStreamInfo.blockedCountries(),
                videoQualityDTOs,
                videoStreamInfo.defaultBufferInterval(),
                sizeRestrictions,
                videoStreamInfo.directStream(),
                videoStreamInfo.inlineStream(),
                videoStreamEndpoint,
                videoStreamInfo.eventId(),
                videoStreamInfo.eventName(),
                videoStreamInfo.sportId(),
                videoStreamInfo.sportName(),
                videoStreamInfo.providerEventId(),
                videoStreamInfo.providerEventName(),
                videoStreamInfo.accountId(),
                videoStreamInfo.videoPlayerConfig(),
                videoStreamInfo.startDateTime(),
                videoStreamInfo.competition(),
                videoStreamInfo.defaultVideoQuality(),
                contentType
        );
    }

    private static VideoQualityDto mapVideoQualityToDto(VideoQuality videoQuality) {
        if (videoQuality == null) {
            return VideoQualityDto.UNRECOGNIZED_VALUE;
        }

        return VideoQualityDto.fromValue(videoQuality.getValue());
    }

    private static SizeRestrictionsDto mapSizeRestrictionsToDto(SizeRestrictions sizeRestrictions) {
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

    private static VideoStreamEndpointDto mapVideoStreamEndpointToDto(VideoStreamEndpoint videoStreamEndpoint) {
        if (videoStreamEndpoint == null) {
            return null;
        }

        return new VideoStreamEndpointDto(
                videoStreamEndpoint.videoEndpoint(),
                videoStreamEndpoint.playerControlParams()
        );
    }

    private static ContentTypeDto mapContentTypeToDto(ContentType contentType) {
        if (contentType == null) {
            return null;
        }

        return ContentTypeDto.fromValue(contentType.getValue());
    }

}
