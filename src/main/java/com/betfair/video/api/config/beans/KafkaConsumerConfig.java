package com.betfair.video.api.config.beans;

import com.betfair.video.api.infra.input.kafka.dto.LiveVideoConfigMessageDto;
import com.betfair.video.api.infra.input.kafka.dto.ScheduleVideoMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupName;

    @Bean
    public ConsumerFactory<String, LiveVideoConfigMessageDto> liveVideoConfigConsumerFactory() {
        Map<String, Object> configProps = getCommonConfigProps();
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LiveVideoConfigMessageDto.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                new JsonDeserializer<>(LiveVideoConfigMessageDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LiveVideoConfigMessageDto> liveVideoConfigKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LiveVideoConfigMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(liveVideoConfigConsumerFactory());

        factory.setCommonErrorHandler(new org.springframework.kafka.listener.DefaultErrorHandler());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, ScheduleVideoMessageDto> scheduleVideoConfigConsumerFactory() {
        Map<String, Object> configProps = getCommonConfigProps();
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ScheduleVideoMessageDto.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                new JsonDeserializer<>(ScheduleVideoMessageDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ScheduleVideoMessageDto> scheduleVideoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ScheduleVideoMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(scheduleVideoConfigConsumerFactory());

        factory.setCommonErrorHandler(new org.springframework.kafka.listener.DefaultErrorHandler());

        return factory;
    }

    private Map<String, Object> getCommonConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupName + "_" + getNodeInstanceName());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configProps;
    }

    private String getNodeInstanceName() {
        StringBuilder scheduleHandlerInstanceName = new StringBuilder();
        InetAddress address = null;
        try {
            address = getLocalIPAddress();
        } catch (Exception e) {
            logger.error("Cannot define server ip address", e);
        }
        if (address != null) {
            scheduleHandlerInstanceName.append(address.getHostAddress());
            scheduleHandlerInstanceName.append(":");
            scheduleHandlerInstanceName.append(address.getCanonicalHostName());
        }

        final int randomNumber = new Random().nextInt();

        scheduleHandlerInstanceName.append(":");
        scheduleHandlerInstanceName.append(randomNumber);
        return scheduleHandlerInstanceName.toString();
    }

    private InetAddress getLocalIPAddress() throws Exception {
        InetAddress candidateAddress = null;
        Enumeration ifaces = NetworkInterface.getNetworkInterfaces();

        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            Enumeration inetAddrs = iface.getInetAddresses();

            while (inetAddrs.hasMoreElements()) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {
                    if (inetAddr.isSiteLocalAddress()) {
                        return inetAddr;
                    }

                    if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }
                }
            }
        }

        if (candidateAddress != null) {
            return candidateAddress;
        }

        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
        }

        return jdkSuppliedAddress;
    }

}
