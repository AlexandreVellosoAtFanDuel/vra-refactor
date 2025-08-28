package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.entity.AuditItem;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.ScheduleItemData;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.port.VideoStreamInfoPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.valueobject.ImportStatus;
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
                "Live Test Event",
                null,
                null,
                null,
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, -10),
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, +10),
                null
        );

        ScheduleItemData overriddenData = new ScheduleItemData(
                "Live Test Event",
                null,
                null,
                null,
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, -10),
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, +10),
                null
        );

        AuditItem auditItem = new AuditItem(1, "system", new Date(), "127.0.0.1");

        ScheduleItem scheduleItem = new ScheduleItem(
                103672720L,
                26,
                "2",
                3,
                null,
                null,
                null,
                null,
                -1,
                300,
                0,
                1,
                1,
                ImportStatus.UPDATED,
                auditItem,
                'A',
                false,
                1,
                null,
                new Date(),
                3,
                providerData,
                overriddenData,
                null
        );

        return Collections.singletonList(scheduleItem);
    }

}
