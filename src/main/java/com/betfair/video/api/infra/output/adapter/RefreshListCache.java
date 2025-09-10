package com.betfair.video.api.infra.output.adapter;

public interface RefreshListCache<T> {

    void insertItemsToCache(T items);

    boolean isCacheExpired();

}
