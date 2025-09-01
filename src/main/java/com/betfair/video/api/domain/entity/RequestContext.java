package com.betfair.video.api.domain.entity;

import java.util.List;

public class RequestContext {
    private String uuid;
    private List<String> resolvedIps;
    private User user;

    public RequestContext(String uuid, List<String> resolvedIps) {
        this.uuid = uuid;
        this.resolvedIps = resolvedIps;
    }

    public RequestContext(String uuid, List<String> resolvedIps, User user) {
        this.uuid = uuid;
        this.resolvedIps = resolvedIps;
        this.user = user;
    }

    public String uuid() {
        return uuid;
    }

    public List<String> resolvedIps() {
        return resolvedIps;
    }

    public User user() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
