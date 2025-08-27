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

        /*
        Use this one for mock data structure reference
        {
            "uniqueVideoId": 159532269,
            "providerId": 34,
            "blockedCountries": "ZA",
            "videoQuality": [],
            "defaultBufferInterval": "1",
            "sizeRestrictions": {
                "fullScreenAllowed": false,
                "airPlayAllowed": false,
                "aspectRatio": "0.61",
                "widthMax": 1080,
                "widthDefault": 480
            },
            "directStream": false,
            "inlineStream": false,
            "videoStreamEndpoint": {
                "playerControlParams": {
                    "widgetName": "match.lmtPlus",
                    "sportName": "football",
                    "matchId": "61378347"
                }
            },
            "eventId": "35394600",
            "eventName": "SV Horn v Austria Klagenfurt",
            "sportId": "1",
            "sportName": "Football",
            "providerEventId": "61378347",
            "providerEventName": "SV HORN v SK AUSTRIA KLAGENFURT",
            "accountId": 100002650,
            "startDateTime": "2025-07-25T17:30:00.000Z",
            "competition": "OFB Cup",
            "defaultVideoQuality": "MEDIUM",
            "contentType": "VIZ"
        }
         */

        ScheduleItemData providerData = new ScheduleItemData(
                "SV Horn v Austria Klagenfurt",
                "venue",
                "country",
                "",
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, -10),
                DateUtils.shiftDateByField(new Date(), Calendar.MINUTE, +10),
                "OFB Cup"
        );

        ScheduleItem scheduleItem = new ScheduleItem(
                159532269L,
                34,
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
