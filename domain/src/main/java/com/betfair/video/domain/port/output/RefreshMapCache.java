package com.betfair.video.domain.port.output;

import java.util.Map;

public interface RefreshMapCache<K, V> {

    void insertItemsToCache(Map<K, V> items);

}
