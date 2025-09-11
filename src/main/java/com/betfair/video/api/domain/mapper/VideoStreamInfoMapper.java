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
import com.betfair.video.api.domain.port.input.GeoRestrictionsUseCase;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class VideoStreamInfoMapper {

    private VideoStreamInfoMapper() {
    }

    public static VideoStreamInfo map(
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
            final GeoRestrictionsUseCase geoRestrictionsUseCase,
            final String eventId,
            final String eventName,
            final String exchangeRaceId
    ) {
        Long uniqueVideoId = null;
        Integer providerId = null;
        String commentaryLanguages = null;
        String blockedCountries = null;
        String sportId = null;
        String providerEventId = null;
        String providerEventName = null;
        String competition = null;
        Date startDateTime = null;

        if (scheduleItem != null) {
            uniqueVideoId = scheduleItem.videoItemId();
            providerId = scheduleItem.providerId();
            commentaryLanguages = scheduleItem.providerLanguage();
            blockedCountries = mapBlockedCountries(scheduleItem, geoRestrictionsUseCase);
            sportId = mapSportId(scheduleItem);
            providerEventId = mapProviderEventId(scheduleItem);
            providerEventName = mapProviderEventName(scheduleItem);
            competition = mapCompetition(scheduleItem);
            startDateTime = mapStartDateTime(scheduleItem);
        }

        if (includeMetadata) {
            ScheduleItemData overridenScheduleItemData = scheduleItem.getActualProviderData();

            if (scheduleItem.betfairSportsType() != null) {
                sportId = String.valueOf(scheduleItem.betfairSportsType());
            }

            if (StringUtils.isNotEmpty(scheduleItem.providerEventId())) {
                providerEventId = scheduleItem.providerEventId();
            }

            if (overridenScheduleItemData != null) {
                if (StringUtils.isNotEmpty(overridenScheduleItemData.getEventName())) {
                    providerEventName = overridenScheduleItemData.getEventName();
                }
                if (StringUtils.isNotEmpty(overridenScheduleItemData.getCompetition())) {
                    competition = overridenScheduleItemData.getCompetition();
                }
                if (overridenScheduleItemData.getStart() != null) {
                    startDateTime = overridenScheduleItemData.getStart();
                }
            }
        }

        return new VideoStreamInfo(
                uniqueVideoId,
                providerId,
                commentaryLanguages,
                blockedCountries,
                mapVideoQualityList(availableVideoQualityValues),
                defaultVideoQuality != null ? defaultVideoQuality.name() : null,
                defaultBufferingValue,
                mapSizeRestrictions(sizeRestrictions),
                isDirectStream,
                isInlineStream,
                contentType,
                mapVideoStreamEndpoint(streamDetails),
                eventId,
                eventName,
                sportId,
                mapSportName(typeSport),
                providerEventId,
                providerEventName,
                null,
                mapAccountId(user),
                exchangeRaceId,
                videoPlayerConfig,
                startDateTime,
                competition,
                null,
                null
        );
    }

    private static String mapBlockedCountries(ScheduleItem scheduleItem, @Context GeoRestrictionsUseCase geoRestrictionsUseCase) {
        String defaultBlockedCountries = geoRestrictionsUseCase != null
                ? geoRestrictionsUseCase.getProviderBlockedCountries(scheduleItem) : null;
        String providerBlockedCountries = scheduleItem.providerData() != null
                ? scheduleItem.providerData().getBlockedCountries() : null;
        String overrideBlockedCountries = scheduleItem.overriddenData() != null
                ? scheduleItem.overriddenData().getBlockedCountries() : null;

        return createAggregatedBlockedCountries(defaultBlockedCountries, providerBlockedCountries, overrideBlockedCountries);
    }

    private static List<VideoQuality> mapVideoQualityList(Set<VideoQuality> availableVideoQualityValues) {
        if (availableVideoQualityValues == null) {
            return null;
        }

        return new ArrayList<>(availableVideoQualityValues);
    }

    private static VideoStreamEndpoint mapVideoStreamEndpoint(StreamDetails streamDetails) {

        String videoQuality = null;
        String videoEndpoint = null;
        Map<String, String> playerControlParams = null;

        if (streamDetails != null) {
            videoQuality = streamDetails.quality() != null ? streamDetails.quality().getValue() : null;
            videoEndpoint = streamDetails.endpoint();
            playerControlParams = streamDetails.params();
        }

        return new VideoStreamEndpoint(
                null,
                videoQuality,
                videoEndpoint,
                playerControlParams
        );
    }

    private static SizeRestrictions mapSizeRestrictions(Map<ConfigurationType, String> sizeRestrictions) {
        if (sizeRestrictions == null || sizeRestrictions.isEmpty()) {
            return null;
        }

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

    private static Long mapAccountId(User user) {
        return user != null ? Long.valueOf(user.accountId()) : null;
    }

    private static String mapSportId(ScheduleItem scheduleItem) {
        return scheduleItem.betfairSportsType() != null ? String.valueOf(scheduleItem.betfairSportsType()) : null;
    }

    private static String mapProviderEventId(ScheduleItem scheduleItem) {
        return StringUtils.isNotEmpty(scheduleItem.providerEventId()) ? scheduleItem.providerEventId() : null;
    }

    private static String mapProviderEventName(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getEventName())
                ? scheduleItemData.getEventName() : null;
    }

    private static String mapCompetition(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null && StringUtils.isNotEmpty(scheduleItemData.getCompetition())
                ? scheduleItemData.getCompetition() : null;
    }

    private static Date mapStartDateTime(ScheduleItem scheduleItem) {
        ScheduleItemData scheduleItemData = scheduleItem.getActualProviderData();
        return scheduleItemData != null ? scheduleItemData.getStart() : null;
    }

    private static String mapSportName(TypeSport typeSport) {
        return typeSport != null ? typeSport.getDescription() : null;
    }

    private static Integer parseInteger(String value) {
        if (!NumberUtils.isCreatable(value)) {
            return null;
        }

        return NumberUtils.createNumber(value).intValue();
    }

    private static String createAggregatedBlockedCountries(String... blockedCountries) {
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
