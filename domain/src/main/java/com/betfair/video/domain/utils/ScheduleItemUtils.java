package com.betfair.video.domain.utils;

import com.betfair.video.domain.dto.entity.ScheduleItem;

import java.util.List;

public class ScheduleItemUtils {

    private ScheduleItemUtils() {
    }

    public static StringBuilder getItemsForLog(List<ScheduleItem> items) {
        StringBuilder itemsToLog = new StringBuilder();
        if (!items.isEmpty()) {
            itemsToLog.append(" [");

            for (ScheduleItem item : items) {
                itemsToLog.append("{").append("videoId=").append(item.videoItemId());
                itemsToLog.append(",videoName=").append(item.getActualProviderData().getEventName()).append("},");
            }

            itemsToLog.append("]");
        }

        return itemsToLog;
    }

    public static ScheduleItem pickMaxScoredStream(String primaryId, List<ScheduleItem> items) {
        // TODO: Implement method
        return null;
    }
}
