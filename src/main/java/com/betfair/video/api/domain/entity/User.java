package com.betfair.video.api.domain.entity;

import java.util.List;

public class User {
    String id;

    String uuid;

    List<String> ipAddresses;

    String countryCode;

    String subDivisionCode;

    Integer dmaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getIpAddress() {
        return ipAddresses;
    }

    public void setIpAddresses(List<String> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSubDivisionCode() {
        return subDivisionCode;
    }

    public void setSubDivisionCode(String subDivisionCode) {
        this.subDivisionCode = subDivisionCode;
    }

    public Integer getDmaId() {
        return dmaId;
    }

    public void setDmaId(Integer dmaId) {
        this.dmaId = dmaId;
    }

}
