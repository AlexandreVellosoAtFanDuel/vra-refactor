package com.betfair.video.api.infra.output.adapter;

import java.util.Map;

public interface RefreshMapCache<K, V> {

    void insertItemsToCache(Map<K, V> items);

}
