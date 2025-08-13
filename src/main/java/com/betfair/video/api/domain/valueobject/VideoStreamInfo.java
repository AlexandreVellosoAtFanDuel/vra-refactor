package com.betfair.video.api.domain.valueobject;

import java.util.Date;
import java.util.List;

public class VideoStreamInfo {

    private Long uniqueVideoId;
    private Integer providerId;
    private String commentaryLanguages;
    private String blockedCountries;
    private List<String> videoQuality;
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

    public final void setTimeformRaceId(String timeformRaceId) {
        if (delegate != null) {
            delegate.setTimeformRaceId(timeformRaceId);
        } else {
            this.timeformRaceId = timeformRaceId;
        }
    }

}
