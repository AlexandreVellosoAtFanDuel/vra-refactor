package com.betfair.video.domain.port.output;

public interface RefreshListCache<T> {

    void insertItemsToCache(T items);

    boolean isCacheExpired();

}
