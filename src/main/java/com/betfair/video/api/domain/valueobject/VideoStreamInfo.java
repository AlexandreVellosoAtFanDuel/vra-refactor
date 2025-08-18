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

    public final void setTimeformRaceId(String timeformRaceId) {
        if (delegate != null) {
            delegate.setTimeformRaceId(timeformRaceId);
        } else {
            this.timeformRaceId = timeformRaceId;
        }
    }

    public void setUniqueVideoId(Long uniqueVideoId) {
        this.uniqueVideoId = uniqueVideoId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public void setCommentaryLanguages(String commentaryLanguages) {
        this.commentaryLanguages = commentaryLanguages;
    }

    public void setBlockedCountries(String blockedCountries) {
        this.blockedCountries = blockedCountries;
    }

    public void setVideoQuality(List<VideoQuality> videoQuality) {
        this.videoQuality = videoQuality;
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

    public void setDefaultBufferInterval(String defaultBufferInterval) {
        this.defaultBufferInterval = defaultBufferInterval;
    }

    public void setSizeRestrictions(SizeRestrictions sizeRestrictions) {
        this.sizeRestrictions = sizeRestrictions;
    }

    public void setDirectStream(Boolean directStream) {
        this.directStream = directStream;
    }

    public void setInlineStream(Boolean inlineStream) {
        this.inlineStream = inlineStream;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setVideoStreamEndpoint(VideoStreamEndpoint videoStreamEndpoint) {
        this.videoStreamEndpoint = videoStreamEndpoint;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public void setProviderEventId(String providerEventId) {
        this.providerEventId = providerEventId;
    }

    public void setProviderEventName(String providerEventName) {
        this.providerEventName = providerEventName;
    }

    public void setAccountId(Long accountId) {
        if (delegate != null) {
            delegate.setAccountId(accountId);
        } else {
            this.accountId = accountId;
        }
    }

    public void setExchangeRaceId(String exchangeRaceId) {
        this.exchangeRaceId = exchangeRaceId;
    }

    public void setVideoPlayerConfig(String videoPlayerConfig) {
        this.videoPlayerConfig = videoPlayerConfig;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public void setDelegate(VideoStreamInfoDelegate delegate) {
        this.delegate = delegate;
    }
}
