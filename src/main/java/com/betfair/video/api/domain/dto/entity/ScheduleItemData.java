package com.betfair.video.api.domain.dto.entity;

import java.util.Date;

public class ScheduleItemData {

    private String eventName;
    private String venue;
    private String country;
    private String blockedCountries;
    private Date start;
    private Date end;
    private String competition;

    public ScheduleItemData(String eventName, String venue, String country, String blockedCountries, Date start, Date end, String competition) {
        this.eventName = eventName;
        this.venue = venue;
        this.country = country;
        this.blockedCountries = blockedCountries;
        this.start = start;
        this.end = end;
        this.competition = competition;
    }

    private ScheduleItemData(ScheduleItemData from) {
        this.eventName = from.getEventName();
        this.venue = from.getVenue();
        this.country = from.getCountry();
        this.blockedCountries = from.getBlockedCountries();
        this.competition = from.getCompetition();
        if (from.getStart() != null) {
            this.start = new Date(from.getStart().getTime());
        }

        if (from.getEnd() != null) {
            this.end = new Date(from.getEnd().getTime());
        }

    }

    public ScheduleItemData clone() {
        return new ScheduleItemData(this);
    }

    public void override(ScheduleItemData overriddenData) {
        if (overriddenData == null) {
            return;
        }

        if (overriddenData.getEventName() != null) {
            this.eventName = overriddenData.getEventName();
        }

        if (overriddenData.getVenue() != null) {
            this.venue = overriddenData.getVenue();
        }

        if (overriddenData.getStart() != null) {
            this.start = overriddenData.getStart();
        }

        if (overriddenData.getEnd() != null) {
            this.end = overriddenData.getEnd();
        }

        if (overriddenData.getBlockedCountries() != null) {
            this.blockedCountries = overriddenData.getBlockedCountries();
        }

        if (overriddenData.getCountry() != null) {
            this.country = overriddenData.getCountry();
        }

        if (overriddenData.getCompetition() != null) {
            this.competition = overriddenData.getCompetition();
        }
    }

    public String getEventName() {
        return eventName;
    }

    public String getVenue() {
        return venue;
    }

    public String getCountry() {
        return country;
    }

    public String getBlockedCountries() {
        return blockedCountries;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getCompetition() {
        return competition;
    }
}
