package com.betfair.video.api.infra.adapter;

import java.util.Map;

public interface RefreshCache<K, V> {

    void insertItemsToCache(Map<K, V> items);

}
