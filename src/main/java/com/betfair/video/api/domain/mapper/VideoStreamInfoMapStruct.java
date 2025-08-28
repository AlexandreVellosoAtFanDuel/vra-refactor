package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.entity.ConfigurationType;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.ScheduleItemData;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import com.betfair.video.api.domain.valueobject.ContentType;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import com.betfair.video.api.domain.valueobject.SizeRestrictions;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.VideoStreamEndpoint;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Mapper(componentModel = "spring")
@Component
public interface VideoStreamInfoMapStruct {

    @Mapping(target = "uniqueVideoId", source = "scheduleItem.videoItemId")
    @Mapping(target = "providerId", source = "scheduleItem.providerId")
    @Mapping(target = "commentaryLanguages", source = "scheduleItem.providerLanguage")
    @Mapping(target = "blockedCountries", source = "scheduleItem", qualifiedByName = "mapBlockedCountries")
    @Mapping(target = "videoQuality", source = "availableVideoQualityValues", qualifiedByName = "mapVideoQualityList")
    @Mapping(target = "videoStreamEndpoint", source = "streamDetails", qualifiedByName = "mapVideoStreamEndpoint")
    @Mapping(target = "sizeRestrictions", source = "sizeRestrictions", qualifiedByName = "mapSizeRestrictions")
    @Mapping(target = "defaultBufferInterval", source = "defaultBufferingValue")
    @Mapping(target = "defaultVideoQuality", source = "defaultVideoQuality")
    @Mapping(target = "videoPlayerConfig", source = "videoPlayerConfig")
    @Mapping(target = "accountId", source = "user", qualifiedByName = "mapAccountId")
    @Mapping(target = "directStream", source = "isDirectStream")
    @Mapping(target = "inlineStream", source = "isInlineStream")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "sportId", source = "scheduleItem", qualifiedByName = "mapSportId")
    @Mapping(target = "providerEventId", source = "scheduleItem", qualifiedByName = "mapProviderEventId")
    @Mapping(target = "providerEventName", source = "scheduleItem", qualifiedByName = "mapProviderEventName")
    @Mapping(target = "competition", source = "scheduleItem", qualifiedByName = "mapCompetition")
    @Mapping(target = "startDateTime", source = "scheduleItem", qualifiedByName = "mapStartDateTime")
    @Mapping(target = "sportName", source = "sportReferenceType", qualifiedByName = "mapSportName")
    @Mapping(target = "eventId", ignore = true) // Set manually in the service
    @Mapping(target = "eventName", ignore = true) // Set manually in the service
    @Mapping(target = "exchangeRaceId", ignore = true) // Set manually in the service
    @Mapping(target = "timeformRaceId", ignore = true)
    @Mapping(target = "delegate", ignore = true)
    @Mapping(target = "rawDefaultVideoQualityValue", ignore = true)
    VideoStreamInfo mapToVideoStreamInfo(
            ScheduleItem scheduleItem,
            StreamDetails streamDetails,
            Set<VideoQuality> availableVideoQualityValues,
            Map<ConfigurationType, String> sizeRestrictions,
            boolean isDirectStream,
            boolean isInlineStream,
            ContentType contentType,
            ReferenceType sportReferenceType,
            VideoQuality defaultVideoQuality,
            String defaultBufferingValue,
            User user,
            boolean includeMetadata,
            String videoPlayerConfig,
            @Context GeoRestrictionsService geoRestrictionsService
    );

    @Named("mapBlockedCountries")
    default String mapBlockedCountries(ScheduleItem scheduleItem, @Context GeoRestrictionsService geoRestrictionsService) {
        String defaultBlockedCountries = geoRestrictionsService != null
                ? geoRestrictionsService.getProviderBlockedCountries(scheduleItem) : null;
        String providerBlockedCountries = scheduleItem.providerData() != null
                ? scheduleItem.providerData().getBlockedCountries() : null;
        String overrideBlockedCountries = scheduleItem.overriddenData() != null
                ? scheduleItem.overriddenData().getBlockedCountries() : null;
        
        return createAggregatedBlockedCountries(defaultBlockedCountries, providerBlockedCountries, overrideBlockedCountries);
    }

    @Named("mapVideoQualityList")
    default List<VideoQuality> mapVideoQualityList(Set<VideoQuality> availableVideoQualityValues) {
        if (availableVideoQualityValues != null) {
            return new ArrayList<>(availableVideoQualityValues);
        }
        return null;
    }

    @Named("mapVideoStreamEndpoint")
    default VideoStreamEndpoint mapVideoStreamEndpoint(StreamDetails streamDetails) {
        VideoStreamEndpoint streamEndpoint = new VideoStreamEndpoint();

        if (streamDetails != null) {
            streamEndpoint.setVideoEndpoint(streamDetails.endpoint());
            streamEndpoint.setVideoQuality(streamDetails.quality() != null ? streamDetails.quality().getValue() : null);
            streamEndpoint.setPlayerControlParams(streamDetails.params());
        }

        return streamEndpoint;
    }

    @Named("mapSizeRestrictions")
    default SizeRestrictions mapSizeRestrictions(Map<ConfigurationType, String> sizeRestrictions) {
        if (sizeRestrictions != null && !sizeRestrictions.isEmpty()) {
            return new SizeRestrictions(
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_WIDTH_PERCENTAGE)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_HEIGHT_PERCENTAGE)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_WIDTH_PIXEL)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_HEIGHT_PIXEL)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_WIDTH_CENTIMETER)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_HEIGHT_CENTIMETER)),
                    BooleanUtils.toBoolean(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_FULLSCREEN_ALLOWED)),
                    BooleanUtils.toBoolean(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_AIRPLAY_ALLOWED)),
                    sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_ASPECT_RATIO),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_MAX_WIDTH)),
                    parseInteger(sizeRestrictions.get(ConfigurationType.SIZE_RESTRICTION_DEAFULT_WIDTH))
            );
        }
        return null;
    }

    @Named("mapAccountId")
    default Long mapAccountId(User user) {
        return user != null ? Long.valueOf(user.accountId()) : null;
    }

    @Named("mapSportId")
    default String mapSportId(ScheduleItem scheduleItem) {
        return scheduleItem.betfairSportsType() != null ? String.valueOf(scheduleItem.betfairSportsType()) : null;
    }

    @Named("mapProviderEventId")
    default String mapProviderEventId(ScheduleItem scheduleItem) {
        return StringUtils.isNotEmpty(scheduleItem.providerEventId()) ? scheduleItem.providerEventId() : null;
    }

    @Named("mapProviderEventName")
    default String mapProviderEventName(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getEventName()) 
                ? scheduleItemData.getEventName() : null;
    }

    @Named("mapCompetition")
    default String mapCompetition(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getCompetition()) 
                ? scheduleItemData.getCompetition() : null;
    }

    @Named("mapStartDateTime")
    default java.util.Date mapStartDateTime(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null ? scheduleItemData.getStart() : null;
    }

    @Named("mapSportName")
    default String mapSportName(ReferenceType sportReferenceType) {
        return sportReferenceType != null ? sportReferenceType.description() : null;
    }

    // Helper methods
    default Integer parseInteger(String value) {
        if (NumberUtils.isCreatable(value)) {
            return NumberUtils.createNumber(value).intValue();
        }
        return null;
    }

    default String createAggregatedBlockedCountries(String... blockedCountries) {
        Set<String> aggregatedBlockedCountries = new TreeSet<>();

        for (String countryList : blockedCountries) {
            if (StringUtils.isNotBlank(countryList)) {
                String[] countries = countryList.split("\\s");
                Collections.addAll(aggregatedBlockedCountries, countries);
            }
        }

        return StringUtils.join(aggregatedBlockedCountries.toArray(), " ");
    }
}
