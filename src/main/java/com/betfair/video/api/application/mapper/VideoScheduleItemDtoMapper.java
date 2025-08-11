package com.betfair.video.api.application.mapper;

import com.betfair.video.api.application.dto.VideoScheduleItemDto;
import com.betfair.video.api.domain.entity.VideoScheduleItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoScheduleItemDtoMapper {

    public VideoScheduleItemDto mapToDto(VideoScheduleItem videoScheduleItem) {
        return new VideoScheduleItemDto();
    }

    public List<VideoScheduleItemDto> mapToDtoList(List<VideoScheduleItem> videoScheduleItems) {
        return videoScheduleItems.stream()
                .map(this::mapToDto)
                .toList();
    }

}
