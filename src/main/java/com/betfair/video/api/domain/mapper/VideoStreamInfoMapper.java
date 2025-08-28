package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.ScheduleItemData;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import com.betfair.video.api.domain.valueobject.ContentType;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class VideoStreamInfoMapper {

    private final VideoStreamInfoMapStruct mapStruct;

    public VideoStreamInfoMapper() {
        this.mapStruct = Mappers.getMapper(VideoStreamInfoMapStruct.class);
    }

    public VideoStreamInfo map(
            final ScheduleItem scheduleItem,
            final StreamDetails streamDetails,
            final Set<VideoQuality> availableVideoQualityValues,
            final Map<ConfigurationType, String> sizeRestrictions,
            final boolean isDirectStream,
            final boolean isInlineStream,
            final ContentType contentType,
            final ReferenceType sportReferenceType,
            final VideoQuality defaultVideoQuality,
            final String defaultBufferingValue,
            final User user,
            final boolean includeMetadata,
            final String videoPlayerConfig,
            final GeoRestrictionsService geoRestrictionsService
    ) {
        VideoStreamInfo videoStreamInfo = mapStruct.mapToVideoStreamInfo(
                scheduleItem,
                streamDetails,
                availableVideoQualityValues,
                sizeRestrictions,
                isDirectStream,
                isInlineStream,
                contentType,
                includeMetadata ? sportReferenceType : null,
                defaultVideoQuality,
                defaultBufferingValue,
                user,
                includeMetadata,
                videoPlayerConfig,
                geoRestrictionsService
        );

        if (includeMetadata) {
            ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
            //for GA. Note - some metadata is returned unconditionally (eventId).
            if (scheduleItem.betfairSportsType() != null) {
                videoStreamInfo.setSportId(String.valueOf(scheduleItem.betfairSportsType()));
            }
            if (StringUtils.isNotEmpty(scheduleItem.providerEventId())) {
                videoStreamInfo.setProviderEventId(scheduleItem.providerEventId());
            }
            if (scheduleItemData != null) {
                if (StringUtils.isNotEmpty(scheduleItemData.getEventName())) {
                    videoStreamInfo.setProviderEventName(scheduleItemData.getEventName());
                }
                if (StringUtils.isNotEmpty(scheduleItemData.getCompetition())) {
                    videoStreamInfo.setCompetition(scheduleItemData.getCompetition());
                }
                if (scheduleItemData.getStart() != null) {
                    videoStreamInfo.setStartDateTime(scheduleItemData.getStart());
                }
            }
            if (sportReferenceType != null) {
                videoStreamInfo.setSportName(sportReferenceType.description());
            }
        }

        // TODO:  String requestedEventId = (String) user.getCurrentContextHolder().params.get(User.CurrentContextHolder.PARAM_STREAM_REQUESTED_EVENT_ID);
        String requestedEventId = null;

        // TODO: String requestedExchangeRaceId = (String) user.getCurrentContextHolder().params.get(User.CurrentContextHolder.EXCHANGE_RACE_ID);
        String requestedExchangeRaceId = null;

        // TODO: String requestedMappingDescription = (String) user.getCurrentContextHolder().params.get(User.CurrentContextHolder.PARAM_STREAM_MAPPING_DESCRIPTION);
        String requestedMappingDescription = null;

        if (StringUtils.isNotEmpty(requestedEventId)) {
            videoStreamInfo.setEventId(requestedEventId);
        }

        if (StringUtils.isNotEmpty(requestedExchangeRaceId)) {
            videoStreamInfo.setExchangeRaceId(requestedExchangeRaceId);
        }
        if (includeMetadata && StringUtils.isNotEmpty(requestedMappingDescription)) {
            videoStreamInfo.setEventName(requestedMappingDescription);
        }

        return videoStreamInfo;
    }
}
