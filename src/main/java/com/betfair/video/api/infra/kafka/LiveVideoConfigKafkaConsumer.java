package com.betfair.video.api.infra.kafka;

import com.betfair.video.api.domain.entity.ConfigurationItem;
import com.betfair.video.api.domain.valueobject.DomainReferenceType;
import com.betfair.video.api.domain.valueobject.ReferenceTypeEnum;
import com.betfair.video.api.domain.valueobject.search.ConfigurationSearchKey;
import com.betfair.video.api.domain.valueobject.search.ReferenceTypeInfoByIdSearchKey;
import com.betfair.video.api.infra.adapter.RefreshCache;
import com.betfair.video.api.infra.kafka.dto.DbConfigDto;
import com.betfair.video.api.infra.kafka.dto.LiveVideoConfigMessageDto;
import com.betfair.video.api.infra.kafka.dto.SportItemDto;
import com.betfair.video.api.infra.mapper.ConfigurationItemMapper;
import com.betfair.video.api.infra.mapper.ConfigurationSearchKeyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LiveVideoConfigKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LiveVideoConfigKafkaConsumer.class);

    private final RefreshCache<ConfigurationSearchKey, ConfigurationItem> configurationItemsRefreshCache;

    private final RefreshCache<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> referenceTypeRefreshCache;

    private final ConfigurationItemMapper configurationItemMapper;

    private final ConfigurationSearchKeyMapper configurationSearchKeyMapper;

    public LiveVideoConfigKafkaConsumer(RefreshCache<ConfigurationSearchKey, ConfigurationItem> configurationItemsRefreshCache,
                                        RefreshCache<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> referenceTypeRefreshCache,
                                        ConfigurationItemMapper configurationItemMapper,
                                        ConfigurationSearchKeyMapper configurationSearchKeyMapper) {
        this.configurationItemsRefreshCache = configurationItemsRefreshCache;
        this.referenceTypeRefreshCache = referenceTypeRefreshCache;
        this.configurationItemMapper = configurationItemMapper;
        this.configurationSearchKeyMapper = configurationSearchKeyMapper;
    }

    @KafkaListener(topics = "${kafka.topic.livevideo-config}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "liveVideoConfigKafkaListenerContainerFactory")
    public void consumeLiveVideoConfig(
            @Payload LiveVideoConfigMessageDto configMessage,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received live video config message from topic: {}, partition: {}, offset: {}", topic, partition, offset);

        final String timestamp = formatTimestamp(configMessage.publishTime());
        logger.info("Message ID: {}, Publish Time: {}", configMessage.msgId(), timestamp);

        updateConfigurationItemCache(configMessage.dbConfigs());
        updateReferenceTypeCache(configMessage.referenceTypes());
    }

    private String formatTimestamp(Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.toString();
    }

    private void updateConfigurationItemCache(List<DbConfigDto> dbConfigs) {
        Map<ConfigurationSearchKey, ConfigurationItem> items = new HashMap<>();

        dbConfigs.forEach(dbConfigDto -> {
            ConfigurationSearchKey key = configurationSearchKeyMapper.map(dbConfigDto);
            ConfigurationItem value = configurationItemMapper.map(dbConfigDto);

            items.put(key, value);
        });

        configurationItemsRefreshCache.insertItemsToCache(items);
    }

    private void updateReferenceTypeCache(Map<String, List<SportItemDto>> typesDto) {
        Map<ReferenceTypeInfoByIdSearchKey, List<DomainReferenceType>> items = new HashMap<>();

        typesDto.forEach((key, value) -> {
            ReferenceTypeInfoByIdSearchKey searchKey = new ReferenceTypeInfoByIdSearchKey(ReferenceTypeEnum.fromValue(key), null);
            List<DomainReferenceType> domainReferenceTypes = value.stream()
                    .map(sportItemDto -> new DomainReferenceType(sportItemDto.type(), sportItemDto.id(), sportItemDto.description()))
                    .toList();

            items.put(searchKey, domainReferenceTypes);
        });

        referenceTypeRefreshCache.insertItemsToCache(items);
    }
}
