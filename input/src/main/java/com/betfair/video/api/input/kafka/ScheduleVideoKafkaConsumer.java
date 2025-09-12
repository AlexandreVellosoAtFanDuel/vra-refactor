package com.betfair.video.api.input.kafka;

import com.betfair.video.api.domain.dto.entity.AuditItem;
import com.betfair.video.api.domain.dto.entity.ProviderEventKey;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.ScheduleItemData;
import com.betfair.video.api.domain.dto.entity.ScheduleItemMapping;
import com.betfair.video.api.domain.dto.entity.ScheduleItemMappingKey;
import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.valueobject.ApprovalStatus;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.dto.valueobject.ImportStatus;
import com.betfair.video.api.domain.dto.valueobject.MappingStatus;
import com.betfair.video.api.domain.port.output.RefreshListCache;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.input.kafka.dto.EventDto;
import com.betfair.video.api.input.kafka.dto.ScheduleVideoDto;
import com.betfair.video.api.input.kafka.dto.ScheduleVideoMessageDto;
import com.betfair.video.api.input.kafka.dto.StreamInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class ScheduleVideoKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleVideoKafkaConsumer.class);

    private final RefreshListCache<List<ScheduleItem>> videoStreamInfoByExternalIdCacheRefresh;

    public ScheduleVideoKafkaConsumer(RefreshListCache<List<ScheduleItem>> videoStreamInfoByExternalIdCacheRefresh) {
        this.videoStreamInfoByExternalIdCacheRefresh = videoStreamInfoByExternalIdCacheRefresh;
    }

    // TODO: Add Fanduel Canada
    // TODO: Add validation if cache is expired, there is a config to set this time private static final "schedule.message.cache.timeout"
    @KafkaListener(topics = {"${kafka.topic.fd.schedule.vid}"},
            groupId = "spring.kafka.consumer.group-id",
            containerFactory = "scheduleVideoKafkaListenerContainerFactory"
    )
    public void consumeScheduleVideo(
            @Payload ScheduleVideoMessageDto scheduleVideoMessage,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received live video schedule message from topic: {}, partition: {}, offset: {}", topic, partition, offset);

        final String timestamp = formatTimestamp(scheduleVideoMessage.publishTime());
        logger.info("Message ID: {}, Publish Time: {}", scheduleVideoMessage.msgId(), timestamp);

        updateScheduleVideoCache(scheduleVideoMessage.schedules());
    }

    private String formatTimestamp(Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.toString();
    }

    private void updateScheduleVideoCache(List<ScheduleVideoDto> schedules) {
        if (!videoStreamInfoByExternalIdCacheRefresh.isCacheExpired()) {
            return;
        }

        try {
            videoStreamInfoByExternalIdCacheRefresh.insertItemsToCache(convert(schedules));
        } catch (Exception e) {
            logger.error("Error when trying to refresh schedule videos cache", e);
        }
    }

    private static List<ScheduleItem> convert(List<ScheduleVideoDto> schedules) {
        if (CollectionUtils.isEmpty(schedules)) {
            return Collections.emptyList();
        }

        List<ScheduleItem> result = new ArrayList<>();
        schedules.stream()
                .flatMap(sportSchedule -> sportSchedule.events().stream())
                .forEach(event -> result.addAll(convertStreams(event.streamInfo(), event)));

        return result;
    }

    // TODO: Use mapper instead of this code
    // TODO: Add converter for Mobile
    // TODO: Add mapper for VIZ streams
    private static List<ScheduleItem> convertStreams(Collection<StreamInfoDto> streamInfos, EventDto parentEvent) {
        if (CollectionUtils.isEmpty(streamInfos)) {
            return Collections.emptyList();
        }

        List<ScheduleItem> result = new ArrayList<>();
        streamInfos.forEach(streamInfo -> {
            if (streamInfo.desktopVideoId() != null) {

                AuditItem auditItem = new AuditItem(
                        streamInfo.desktopVideoInfo().modifiedBySystemId(),
                        streamInfo.desktopVideoInfo().modifiedByUser(),
                        streamInfo.desktopVideoInfo().modifiedDate() != null ? new Date(streamInfo.desktopVideoInfo().modifiedDate()) : null,
                        streamInfo.desktopVideoInfo().modifiedByIp()
                );

                int leadTime = streamInfo.desktopVideoInfo().leadTime() != null ? streamInfo.desktopVideoInfo().leadTime() : 0;
                int trailTime = streamInfo.desktopVideoInfo().trailTime() != null ? streamInfo.desktopVideoInfo().trailTime() : 0;

                ScheduleItemData providerData = new ScheduleItemData(
                        streamInfo.desktopVideoInfo().providerEventName(),
                        streamInfo.desktopVideoInfo().providerVenue(),
                        parentEvent.country(),
                        !CollectionUtils.isEmpty(streamInfo.geoblocked()) ? String.join(" ", streamInfo.geoblocked()) : null,
                        DateUtils.shiftDateByField(new Date(streamInfo.streamTime()), Calendar.SECOND, leadTime),
                        streamInfo.streamEndTime() != null ? DateUtils.shiftDateByField(new Date(streamInfo.streamEndTime()), Calendar.SECOND, -trailTime) : null,
                        streamInfo.desktopVideoInfo().providerCompetition()
                );

                ProviderEventKey eventKey = new ProviderEventKey(
                        ExternalIdSource.BETFAIR_EVENT.getProviderId(),
                        String.valueOf(parentEvent.eventId()),
                        String.format("EVENT:%s", parentEvent.eventId())
                );

                ScheduleItemMappingKey mappingKey = new ScheduleItemMappingKey(
                        String.valueOf(streamInfo.desktopVideoId()),
                        eventKey
                );

                ScheduleItemMapping mapping = new ScheduleItemMapping(
                        mappingKey,
                        parentEvent.eventDescription(),
                        null,
                        streamInfo.desktopVideoInfo().rampId(),
                        parentEvent.meetingName(),
                        parentEvent.venue(),
                        null,
                        streamInfo.desktopVideoInfo().mappingStatus() != null ? MappingStatus.fromValue(streamInfo.desktopVideoInfo().mappingStatus()).getStatus() : null,
                        null,
                        streamInfo.desktopVideoInfo().mappingApprovalStatus() != null ? ApprovalStatus.fromValue(streamInfo.desktopVideoInfo().mappingApprovalStatus()).getStatus() : null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        streamInfo.desktopVideoInfo().mappingInexactManuallyApproved(),
                        streamInfo.desktopVideoInfo().mappingCreatedDate() != null ? new Date(streamInfo.desktopVideoInfo().mappingCreatedDate()) : null,
                        null
                );

                ScheduleItem scheduleItem = new ScheduleItem(
                        streamInfo.desktopVideoId(),
                        streamInfo.providerId(),
                        streamInfo.desktopVideoInfo().providerEventId(),
                        streamInfo.providerSportsType(),
                        streamInfo.desktopVideoInfo().commentaryLanguages(),
                        false,
                        null,
                        false,
                        parentEvent.sportId(),
                        leadTime,
                        trailTime,
                        TypeChannel.WEB.getId(),
                        streamInfo.desktopVideoInfo().channelSubTypeId(),
                        ImportStatus.fromValue(streamInfo.desktopVideoInfo().importStatus()),
                        auditItem,
                        streamInfo.desktopVideoInfo().approvalStatus(),
                        streamInfo.desktopVideoInfo().reviewStatus(),
                        TypeStream.fromValue(streamInfo.streamType()).getId(),
                        false,
                        null,
                        3,
                        providerData,
                        providerData,
                        Set.of(mapping)
                );

                result.add(scheduleItem);
            }
        });

        return result;
    }

}
