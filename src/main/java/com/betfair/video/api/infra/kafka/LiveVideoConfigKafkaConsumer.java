package com.betfair.video.api.infra.kafka;

import com.betfair.video.api.infra.kafka.dto.DbConfigDto;
import com.betfair.video.api.infra.kafka.dto.LiveVideoConfigMessageDto;
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
import java.util.List;
import java.util.Map;

@Component
public class LiveVideoConfigKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LiveVideoConfigKafkaConsumer.class);

    @KafkaListener(topics = "${kafka.topic.livevideo-config}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "liveVideoConfigKafkaListenerContainerFactory")
    public void consumeLiveVideoConfig(
            @Payload LiveVideoConfigMessageDto configMessage,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received live video config message from topic: {}, partition: {}, offset: {}",
                topic, partition, offset);
        logger.info("Message ID: {}, Publish Time: {}",
                configMessage.msgId(),
                formatTimestamp(configMessage.publishTime()));

        // TODO: Send this response to ConfigurationItemCacheManager
        List<DbConfigDto> dbConfigs = configMessage.dbConfigs();

        // TODO: Use this reference types to set the cache instead of TypeReference
        Map<String, Object> referenceTypes = configMessage.referenceTypes();
    }

    private String formatTimestamp(Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.toString();
    }
}
