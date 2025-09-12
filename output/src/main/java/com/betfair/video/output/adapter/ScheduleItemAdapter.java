package com.betfair.video.output.adapter;

import com.betfair.video.domain.dto.entity.ScheduleItem;
import com.betfair.video.domain.port.output.RefreshListCache;
import com.betfair.video.domain.port.output.ScheduleItemPort;
import com.hazelcast.collection.IList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleItemAdapter implements ScheduleItemPort, RefreshListCache<List<ScheduleItem>> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleItemAdapter.class);

    private final IList<ScheduleItem> scheduleItemCache;

    public ScheduleItemAdapter(IList<ScheduleItem> scheduleItemCache) {
        this.scheduleItemCache = scheduleItemCache;
    }

    @Override
    public List<ScheduleItem> getAllAvailableEvents() {
        return scheduleItemCache;
    }

    @Override
    public void insertItemsToCache(List<ScheduleItem> items) {
        if (isCacheExpired()) {
            logger.info("Revalidating schedule items cache with {} items", items.size());

            scheduleItemCache.clear();
            scheduleItemCache.addAll(items);
        }
    }

    @Override
    public boolean isCacheExpired() {
        // TODO: Implement this method
        return true;
    }
}
