package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.dto.valueobject.ImportStatus;
import com.betfair.video.api.domain.exception.DataIsNotReadyException;
import com.betfair.video.api.domain.port.output.ScheduleItemPort;
import com.betfair.video.api.domain.port.output.VideoStreamInfoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class VideoStreamInfoAdapter implements VideoStreamInfoPort {

    private static final Logger logger = LoggerFactory.getLogger(VideoStreamInfoAdapter.class);

    private static final Set<ImportStatus> ALLOWED_MAPPING_STATUSES = EnumSet.of(ImportStatus.NEW, ImportStatus.UPDATED, ImportStatus.OVERRIDDEN);

    private final ScheduleItemPort scheduleItemPort;

    public VideoStreamInfoAdapter(ScheduleItemPort scheduleItemPort) {
        this.scheduleItemPort = scheduleItemPort;
    }

    @Override
    public List<ScheduleItem> getVideoStreamInfoByExternalId(VideoStreamInfoByExternalIdSearchKey searchKey) throws DataIsNotReadyException {
        UUID tId = UUID.randomUUID();
        List<ScheduleItem> availableEvents = scheduleItemPort.getAllAvailableEvents();

        if (CollectionUtils.isEmpty(availableEvents)) {
            throw new DataIsNotReadyException("No data available in cache. Probably VRA didn't receive schedule message or it was empty.");
        }

        logger.info("[{}] Retrieved {} cached schedule items", tId, availableEvents.size());

        List<ScheduleItem> result = applyCommonFilters(availableEvents.stream(), searchKey)
                .filter(scheduleItem -> scheduleItem.mappings().stream().findFirst().get().scheduleItemMappingKey() != null)
                .filter(scheduleItem -> scheduleItem.mappings().stream().findFirst().get().scheduleItemMappingKey().providerEventKey() != null)
                .filter(scheduleItem -> searchKey.getSecondaryId() == null || searchKey.getSecondaryId().equals(scheduleItem.mappings().stream().findFirst().get().scheduleItemMappingKey().providerEventKey().secondaryId()))
                .filter(scheduleItem -> !(searchKey.getExternalIdSource() != null && searchKey.getExternalIdSource().getProviderId() != null) || searchKey.getExternalIdSource().getProviderId().equals(scheduleItem.mappings().stream().findFirst().get().scheduleItemMappingKey().providerEventKey().providerId()))
                .filter(scheduleItem -> searchKey.getPrimaryId().equals(scheduleItem.mappings().stream().findFirst().get().scheduleItemMappingKey().providerEventKey().primaryId()))
                .toList();

        logger.info("[{}] Found {} cached schedule items", tId, result.size());
        return result;
    }

    private Stream<ScheduleItem> applyCommonFilters(Stream<ScheduleItem> items, VRAStreamSearchKey searchKey) {
        return items.filter(scheduleItem -> searchKey.getProviderId() == null || searchKey.getProviderId().equals(scheduleItem.providerId()))
                .filter(scheduleItem -> searchKey.getChannelTypeId() == null || searchKey.getChannelTypeId().equals(scheduleItem.videoChannelType()))
                .filter(scheduleItem -> CollectionUtils.isEmpty(searchKey.getChannelSubTypeIds()) || searchKey.getChannelSubTypeIds().contains(scheduleItem.videoChanelSubType()))
                .filter(scheduleItem -> CollectionUtils.isEmpty(searchKey.getStreamTypeIds()) || searchKey.getStreamTypeIds().contains(scheduleItem.streamTypeId()))
                .filter(scheduleItem -> ImportStatus.DELETED != scheduleItem.importStatus())
                .filter(scheduleItem -> scheduleItem.mappings().stream().findFirst().isPresent())
                .filter(scheduleItem -> ALLOWED_MAPPING_STATUSES.contains(scheduleItem.mappings().stream().findFirst().get().mappingStatus()));
    }

}
