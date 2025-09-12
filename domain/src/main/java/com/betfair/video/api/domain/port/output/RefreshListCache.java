package com.betfair.video.api.domain.port.output;

public interface RefreshListCache<T> {

    void insertItemsToCache(T items);

    boolean isCacheExpired();

}
