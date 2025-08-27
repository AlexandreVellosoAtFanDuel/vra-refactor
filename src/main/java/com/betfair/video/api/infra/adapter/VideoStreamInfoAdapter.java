package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.ScheduleItemData;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.port.VideoStreamInfoPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class VideoStreamInfoAdapter implements VideoStreamInfoPort {

    @Override
    public List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException {

        // TODO: Implement actual data fetching logic here

        ScheduleItemData providerData = new ScheduleItemData(
                "eventName",
                "venue",
                "country",
                "",
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, -10),
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, +10),
                "competition"
        );

        ScheduleItem scheduleItem = new ScheduleItem(
                1L,
                1,
                "providerEventId",
                "providerSportsType",
                "providerLanguage",
                false,
                "providerStreamingUrl",
                false,
                1,
                1,
                1,
                1,
                null,
                null,
                null,
                null,
                1,
                null,
                new Date(),
                3,
                providerData,
                null,
                null
        );

        return Collections.singletonList(scheduleItem);
    }

}
