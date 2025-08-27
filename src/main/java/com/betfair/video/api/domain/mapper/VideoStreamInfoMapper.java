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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class VideoStreamInfoMapper {

    private ScheduleItem scheduleItem;
    private StreamDetails streamDetails;
    private Set<VideoQuality> availableVideoQualityValues;
    private GeoRestrictionsService geoRestrictionsService;
    private Map<ConfigurationType, String> sizeRestrictions;
    private boolean isDirectStream;
    private boolean isInlineStream;
    private ContentType contentType;
    private ReferenceType sportReferenceType;
    private VideoQuality defaultVideoQuality;
    private String defaultBufferingValue;
    private User user;
    private boolean includeMetadata;
    private String videoPlayerConfig;

    public VideoStreamInfoMapper(ScheduleItem scheduleItem,
                                 StreamDetails streamDetails,
                                 GeoRestrictionsService geoRestrictionsService,
                                 boolean includeMetadata) {
        this.scheduleItem = scheduleItem;
        this.streamDetails = streamDetails;
        this.geoRestrictionsService = geoRestrictionsService;
        this.includeMetadata = includeMetadata;
    }

    public VideoStreamInfo generateResponse() {
        VideoStreamInfo videoStreamInfo = new VideoStreamInfo();

        videoStreamInfo.setUniqueVideoId(scheduleItem.videoItemId());
        videoStreamInfo.setProviderId(scheduleItem.providerId());
        videoStreamInfo.setCommentaryLanguages(scheduleItem.providerLanguage());

        //process blocked countries
        String defaultBlockedCountries = geoRestrictionsService != null
                ? geoRestrictionsService.getProviderBlockedCountries(scheduleItem) : null;
        String providerBlockedCountries = scheduleItem.providerData() != null
                ? scheduleItem.providerData().getBlockedCountries() : null;
        String overrideBlockedCountries = scheduleItem.overriddenData() != null
                ? scheduleItem.overriddenData().getBlockedCountries() : null;
        String aggregatedBlockedCountries = createAggregatedBlockedCountries(
                defaultBlockedCountries, providerBlockedCountries, overrideBlockedCountries);

        if (StringUtils.isNotBlank(aggregatedBlockedCountries)) {
            videoStreamInfo.setBlockedCountries(aggregatedBlockedCountries);
        }

        videoStreamInfo.setDirectStream(isDirectStream);
        videoStreamInfo.setInlineStream(isInlineStream);
        videoStreamInfo.setContentType(contentType);
        videoStreamInfo.setVideoQuality(createVideoQualitiesList(availableVideoQualityValues));
        videoStreamInfo.setVideoStreamEndpoint(createVideoStreamEndpoint(streamDetails));
        videoStreamInfo.setSizeRestrictions(getSizeRestrictions());
        videoStreamInfo.setDefaultBufferInterval(defaultBufferingValue);
        videoStreamInfo.setDefaultVideoQuality(defaultVideoQuality);
        videoStreamInfo.setVideoPlayerConfig(videoPlayerConfig);
        videoStreamInfo.setAccountId(Long.valueOf(user.accountId()));

        if (this.includeMetadata) {
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
            if (this.sportReferenceType != null) {
                videoStreamInfo.setSportName(this.sportReferenceType.description());
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
        if (this.includeMetadata && StringUtils.isNotEmpty(requestedMappingDescription)) {
            videoStreamInfo.setEventName(requestedMappingDescription);
        }

        return videoStreamInfo;
    }

    private SizeRestrictions getSizeRestrictions() {
        if (!sizeRestrictions.isEmpty()) {
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

    private List<VideoQuality> createVideoQualitiesList(Set<VideoQuality> availableVideoQualityValues) {
        if (availableVideoQualityValues != null) {
            return new ArrayList<>(availableVideoQualityValues);
        }

        return null;
    }

    private VideoStreamEndpoint createVideoStreamEndpoint(StreamDetails streamDetails) {
        VideoStreamEndpoint streamEndpoint = new VideoStreamEndpoint();

        if (streamDetails != null) {
            streamEndpoint.setVideoEndpoint(streamDetails.endpoint());
            streamEndpoint.setVideoQuality(streamDetails.quality() != null ? streamDetails.quality().getValue() : null);
            streamEndpoint.setPlayerControlParams(streamDetails.params());
        }

        return streamEndpoint;
    }

    private Integer parseInteger(String value) {
        if (NumberUtils.isNumber(value)) {
            return NumberUtils.createNumber(value).intValue();
        }
        return null;
    }

    private String createAggregatedBlockedCountries(String... blockedCountries) {
        Set<String> aggregatedBlockedCountries = new TreeSet();
        String[] var2 = blockedCountries;
        int var3 = blockedCountries.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String countryList = var2[var4];
            if (StringUtils.isNotBlank(countryList)) {
                String[] countries = countryList.split("\\s");
                Collections.addAll(aggregatedBlockedCountries, countries);
            }
        }

        return StringUtils.join(aggregatedBlockedCountries.toArray(), " ");
    }

    public void setAvailableVideoQualityValues(Set<VideoQuality> availableVideoQualityValues) {
        this.availableVideoQualityValues = availableVideoQualityValues;
    }

    public void setSizeRestrictions(Map<ConfigurationType, String> sizeRestrictions) {
        this.sizeRestrictions = sizeRestrictions;
    }

    public void setDirectStream(boolean directStream) {
        isDirectStream = directStream;
    }

    public void setInlineStream(boolean inlineStream) {
        isInlineStream = inlineStream;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setSportReferenceType(ReferenceType sportReferenceType) {
        this.sportReferenceType = sportReferenceType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVideoPlayerConfig(String videoPlayerConfig) {
        this.videoPlayerConfig = videoPlayerConfig;
    }

    public void setDefaultBufferingValue(String defaultBufferingInterval) {
        this.defaultBufferingValue = defaultBufferingInterval;
    }

    public void setDefaultVideoQuality(VideoQuality videoQuality) {
        this.defaultVideoQuality = videoQuality;
    }
}
