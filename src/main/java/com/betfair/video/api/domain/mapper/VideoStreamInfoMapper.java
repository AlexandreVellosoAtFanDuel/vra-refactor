package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.dto.entity.ConfigurationType;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.ScheduleItemData;
import com.betfair.video.api.domain.dto.entity.TypeSport;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.SizeRestrictions;
import com.betfair.video.api.domain.dto.valueobject.StreamDetails;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamEndpoint;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.service.GeoRestrictionsService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class VideoStreamInfoMapper {

    public VideoStreamInfo map(
            final ScheduleItem scheduleItem,
            final StreamDetails streamDetails,
            final Set<VideoQuality> availableVideoQualityValues,
            final Map<ConfigurationType, String> sizeRestrictions,
            final boolean isDirectStream,
            final boolean isInlineStream,
            final ContentType contentType,
            final TypeSport typeSport,
            final VideoQuality defaultVideoQuality,
            final String defaultBufferingValue,
            final User user,
            final boolean includeMetadata,
            final String videoPlayerConfig,
            final GeoRestrictionsService geoRestrictionsService,
            final String eventId,
            final String eventName,
            final String exchangeRaceId
    ) {
        VideoStreamInfo videoStreamInfo = mapToVideoStreamInfo(
                scheduleItem,
                streamDetails,
                availableVideoQualityValues,
                sizeRestrictions,
                isDirectStream,
                isInlineStream,
                contentType,
                includeMetadata ? typeSport : null,
                defaultVideoQuality,
                defaultBufferingValue,
                user,
                videoPlayerConfig,
                geoRestrictionsService,
                eventId,
                eventName,
                exchangeRaceId
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
            if (typeSport != null) {
                videoStreamInfo.setSportName(typeSport.getDescription());
            }
        }

        return videoStreamInfo;
    }

    private VideoStreamInfo mapToVideoStreamInfo(
            ScheduleItem scheduleItem,
            StreamDetails streamDetails,
            Set<VideoQuality> availableVideoQualityValues,
            Map<ConfigurationType, String> sizeRestrictions,
            boolean isDirectStream,
            boolean isInlineStream,
            ContentType contentType,
            TypeSport typeSport,
            VideoQuality defaultVideoQuality,
            String defaultBufferingValue,
            User user,
            String videoPlayerConfig,
            GeoRestrictionsService geoRestrictionsService,
            String eventId,
            String eventName,
            String exchangeRaceId
    ) {
        if ( scheduleItem == null && streamDetails == null && availableVideoQualityValues == null && sizeRestrictions == null && contentType == null && typeSport == null && defaultVideoQuality == null && defaultBufferingValue == null && user == null && videoPlayerConfig == null && eventId == null && eventName == null && exchangeRaceId == null ) {
            return null;
        }

        VideoStreamInfo videoStreamInfo = new VideoStreamInfo();

        if ( scheduleItem != null ) {
            videoStreamInfo.setUniqueVideoId( scheduleItem.videoItemId() );
            videoStreamInfo.setProviderId( scheduleItem.providerId() );
            videoStreamInfo.setCommentaryLanguages( scheduleItem.providerLanguage() );
            videoStreamInfo.setBlockedCountries( mapBlockedCountries( scheduleItem, geoRestrictionsService ) );
            videoStreamInfo.setSportId( mapSportId( scheduleItem ) );
            videoStreamInfo.setProviderEventId( mapProviderEventId( scheduleItem ) );
            videoStreamInfo.setProviderEventName( mapProviderEventName( scheduleItem ) );
            videoStreamInfo.setCompetition( mapCompetition( scheduleItem ) );
            videoStreamInfo.setStartDateTime( mapStartDateTime( scheduleItem ) );
        }
        videoStreamInfo.setVideoStreamEndpoint( mapVideoStreamEndpoint( streamDetails ) );
        videoStreamInfo.setVideoQuality( mapVideoQualityList( availableVideoQualityValues ) );
        videoStreamInfo.setSizeRestrictions( mapSizeRestrictions( sizeRestrictions ) );
        videoStreamInfo.setDirectStream( isDirectStream );
        videoStreamInfo.setInlineStream( isInlineStream );
        videoStreamInfo.setContentType( contentType );
        videoStreamInfo.setSportName( mapSportName( typeSport ) );
        if ( defaultVideoQuality != null ) {
            videoStreamInfo.setDefaultVideoQuality( defaultVideoQuality.name() );
        }
        videoStreamInfo.setDefaultBufferInterval( defaultBufferingValue );
        videoStreamInfo.setAccountId( mapAccountId( user ) );
        videoStreamInfo.setVideoPlayerConfig( videoPlayerConfig );

        return videoStreamInfo;
    }

    private String mapBlockedCountries(ScheduleItem scheduleItem, @Context GeoRestrictionsService geoRestrictionsService) {
        String defaultBlockedCountries = geoRestrictionsService != null
                ? geoRestrictionsService.getProviderBlockedCountries(scheduleItem) : null;
        String providerBlockedCountries = scheduleItem.providerData() != null
                ? scheduleItem.providerData().getBlockedCountries() : null;
        String overrideBlockedCountries = scheduleItem.overriddenData() != null
                ? scheduleItem.overriddenData().getBlockedCountries() : null;

        return createAggregatedBlockedCountries(defaultBlockedCountries, providerBlockedCountries, overrideBlockedCountries);
    }

    private List<VideoQuality> mapVideoQualityList(Set<VideoQuality> availableVideoQualityValues) {
        if (availableVideoQualityValues != null) {
            return new ArrayList<>(availableVideoQualityValues);
        }
        return null;
    }

    private VideoStreamEndpoint mapVideoStreamEndpoint(StreamDetails streamDetails) {
        VideoStreamEndpoint streamEndpoint = new VideoStreamEndpoint();

        if (streamDetails != null) {
            streamEndpoint.setVideoEndpoint(streamDetails.endpoint());
            streamEndpoint.setVideoQuality(streamDetails.quality() != null ? streamDetails.quality().getValue() : null);
            streamEndpoint.setPlayerControlParams(streamDetails.params());
        }

        return streamEndpoint;
    }

    private SizeRestrictions mapSizeRestrictions(Map<ConfigurationType, String> sizeRestrictions) {
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

    private Long mapAccountId(User user) {
        return user != null ? Long.valueOf(user.accountId()) : null;
    }

    private String mapSportId(ScheduleItem scheduleItem) {
        return scheduleItem.betfairSportsType() != null ? String.valueOf(scheduleItem.betfairSportsType()) : null;
    }

    private String mapProviderEventId(ScheduleItem scheduleItem) {
        return StringUtils.isNotEmpty(scheduleItem.providerEventId()) ? scheduleItem.providerEventId() : null;
    }

    private String mapProviderEventName(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getEventName())
                ? scheduleItemData.getEventName() : null;
    }

    private String mapCompetition(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getCompetition())
                ? scheduleItemData.getCompetition() : null;
    }

    private java.util.Date mapStartDateTime(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null ? scheduleItemData.getStart() : null;
    }

    private String mapSportName(TypeSport typeSport) {
        return typeSport != null ? typeSport.getDescription() : null;
    }

    private Integer parseInteger(String value) {
        if (!NumberUtils.isCreatable(value)) {
            return null;
        }

        return NumberUtils.createNumber(value).intValue();
    }

    private String createAggregatedBlockedCountries(String... blockedCountries) {
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
