package com.betfair.video.api.domain.valueobject;

import java.util.Date;
import java.util.List;

public class VideoStreamInfo {

    private Long uniqueVideoId;
    private Integer providerId;
    private String commentaryLanguages;
    private String blockedCountries;
    private List<VideoQuality> videoQuality;
    private String defaultVideoQuality;
    private String defaultBufferInterval;
    private SizeRestrictions sizeRestrictions;
    private Boolean directStream;
    private Boolean inlineStream;
    private ContentType contentType;
    private VideoStreamEndpoint videoStreamEndpoint;
    private String eventId;
    private String eventName;
    private String sportId;
    private String sportName;
    private String providerEventId;
    private String providerEventName;
    private String timeformRaceId;
    private Long accountId;
    private String exchangeRaceId;
    private String videoPlayerConfig;
    private Date startDateTime;
    private String competition;
    private VideoStreamInfoDelegate delegate;
    private String rawDefaultVideoQualityValue;

    public Long getUniqueVideoId() {
        return uniqueVideoId;
    }

    public void setUniqueVideoId(Long uniqueVideoId) {
        this.uniqueVideoId = uniqueVideoId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getCommentaryLanguages() {
        return commentaryLanguages;
    }

    public void setCommentaryLanguages(String commentaryLanguages) {
        this.commentaryLanguages = commentaryLanguages;
    }

    public String getBlockedCountries() {
        return blockedCountries;
    }

    public void setBlockedCountries(String blockedCountries) {
        this.blockedCountries = blockedCountries;
    }

    public List<VideoQuality> getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(List<VideoQuality> videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getDefaultVideoQuality() {
        return defaultVideoQuality;
    }

    public void setDefaultVideoQuality(String defaultVideoQuality) {
        this.defaultVideoQuality = defaultVideoQuality;
    }

    public final void setDefaultVideoQuality(VideoQuality defaultVideoQuality) {
        if (defaultVideoQuality == VideoQuality.UNRECOGNIZED_VALUE) {
            throw new IllegalArgumentException("UNRECOGNIZED_VALUE reserved for soft enum deserialisation handling");
        }
        if (delegate != null) {
            delegate.setDefaultVideoQuality(defaultVideoQuality);
        } else {
            this.defaultVideoQuality = defaultVideoQuality.getValue();
        }
        if (delegate != null) {
            delegate.setRawDefaultVideoQualityValue(defaultVideoQuality != null ? defaultVideoQuality.name() : null);
        } else {
            this.rawDefaultVideoQualityValue = defaultVideoQuality != null ? defaultVideoQuality.name() : null;
        }
    }

    public String getDefaultBufferInterval() {
        return defaultBufferInterval;
    }

    public void setDefaultBufferInterval(String defaultBufferInterval) {
        this.defaultBufferInterval = defaultBufferInterval;
    }

    public SizeRestrictions getSizeRestrictions() {
        return sizeRestrictions;
    }

    public void setSizeRestrictions(SizeRestrictions sizeRestrictions) {
        this.sizeRestrictions = sizeRestrictions;
    }

    public Boolean getDirectStream() {
        return directStream;
    }

    public void setDirectStream(Boolean directStream) {
        this.directStream = directStream;
    }

    public Boolean getInlineStream() {
        return inlineStream;
    }

    public void setInlineStream(Boolean inlineStream) {
        this.inlineStream = inlineStream;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public VideoStreamEndpoint getVideoStreamEndpoint() {
        return videoStreamEndpoint;
    }

    public void setVideoStreamEndpoint(VideoStreamEndpoint videoStreamEndpoint) {
        this.videoStreamEndpoint = videoStreamEndpoint;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getProviderEventId() {
        return providerEventId;
    }

    public void setProviderEventId(String providerEventId) {
        this.providerEventId = providerEventId;
    }

    public String getProviderEventName() {
        return providerEventName;
    }

    public void setProviderEventName(String providerEventName) {
        this.providerEventName = providerEventName;
    }

    public String getTimeformRaceId() {
        return timeformRaceId;
    }

    public void setTimeformRaceId(String timeformRaceId) {
        this.timeformRaceId = timeformRaceId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        if (delegate != null) {
            delegate.setAccountId(accountId);
        } else {
            this.accountId = accountId;
        }
    }

    public String getExchangeRaceId() {
        return exchangeRaceId;
    }

    public void setExchangeRaceId(String exchangeRaceId) {
        this.exchangeRaceId = exchangeRaceId;
    }

    public String getVideoPlayerConfig() {
        return videoPlayerConfig;
    }

    public void setVideoPlayerConfig(String videoPlayerConfig) {
        this.videoPlayerConfig = videoPlayerConfig;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public VideoStreamInfoDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(VideoStreamInfoDelegate delegate) {
        this.delegate = delegate;
    }

    public String getRawDefaultVideoQualityValue() {
        return rawDefaultVideoQualityValue;
    }

    public void setRawDefaultVideoQualityValue(String rawDefaultVideoQualityValue) {
        this.rawDefaultVideoQualityValue = rawDefaultVideoQualityValue;
    }
}
