package com.betfair.video.api.application.mapper;

import com.betfair.video.api.application.dto.VideoStreamInfoDto;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import org.springframework.stereotype.Component;

@Component
public class VideoStreamInfoDtoMapper {

    public VideoStreamInfoDto mapToDto(VideoStreamInfo videoStreamInfo) {
        return new VideoStreamInfoDto();
    }

}
