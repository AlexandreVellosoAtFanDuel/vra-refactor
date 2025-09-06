package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.dto.entity.AuditItem;
import com.betfair.video.api.domain.dto.entity.Provider;
import com.betfair.video.api.domain.dto.entity.ProviderEventKey;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.ScheduleItemData;
import com.betfair.video.api.domain.dto.entity.ScheduleItemMappingKey;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.ImportStatus;
import com.betfair.video.api.domain.dto.valueobject.search.VideoStreamInfoSearchKeyWrapper;
import com.betfair.video.api.domain.dto.valueobject.ScheduleItemMapper;
import com.betfair.video.api.domain.port.output.ScheduleItemPort;
import com.betfair.video.api.domain.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class ScheduleItemAdapter implements ScheduleItemPort {

    @Override
    public ScheduleItem getScheduleItemByStreamKey(VideoStreamInfoSearchKeyWrapper videoStreamInfoSearchKeyWrapper, User user) {
        // TODO: Implement this method
        return null;
    }

    @Override
    public List<ScheduleItem> getAllAvailableEvents() {
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

        ScheduleItemMappingKey scheduleItemMappingKey = new ScheduleItemMappingKey("103672720", new ProviderEventKey(7, "33987267", "EVENT:33987267"));

        Set<ScheduleItemMapper> mappings = Set.of(new ScheduleItemMapper(
                null,
                null,
                scheduleItemMappingKey,
                "San Francisco 49ers @ Kansas City Chiefs",
                ImportStatus.NEW
        ));

        /*
        ScheduleItemMapping{scheduleItemMappingKey=MappingProviderEventKey{videoItemId=103672720, providerEventKey=ProviderEventKey{providerId=7, primaryId=33987267, secondaryId=EVENT:33987267}}, mappingDescription='San Francisco 49ers @ Kansas City Chiefs', exchangeRaceId='null', rampId='null', meetingName='null', venueName='null', winMarketName='null', mappingSt=O, approvalSt=A, matchingTypeId=null, facetStartTime=null, specialLogicMatchedItemScore=null, editDistanceMatchedItemScore=null, usedMatchers='null', totalMatchedItemScore=null, manuallyApprovedFlag=null, createdDate=Mon Sep 09 11:10:28 GMT 2024, audit=null}
         */

        ScheduleItem scheduleItem = new ScheduleItem(
                103672720L,
                Provider.BETRADAR_V2.getId(),
                "5265131",
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
                mappings
        );

        return Collections.singletonList(scheduleItem);
    }

}
