package ru.practicum.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import ru.practicum.deserializer.EventsSimilarityAvroDeserializer;
import ru.practicum.deserializer.UserActionAvroDeserializer;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.properties.CustomProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CustomProperties.class)
public class KafkaConsumerConfig {

    private final CustomProperties customProperties;

    private Map<String, Object> getCommonConsumerProperties(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, customProperties.getKafka().getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, customProperties.getKafka().getAutoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, customProperties.getKafka().getEnableAutoCommit());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, customProperties.getKafka().getMaxPollRecords());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        // Дополнительные настройки - все Integer
        if (customProperties.getKafka().getMaxPollIntervalMs() != null) {
            props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, customProperties.getKafka().getMaxPollIntervalMs());
        }
        if (customProperties.getKafka().getSessionTimeoutMs() != null) {
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, customProperties.getKafka().getSessionTimeoutMs());
        }
        if (customProperties.getKafka().getHeartbeatIntervalMs() != null) {
            props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, customProperties.getKafka().getHeartbeatIntervalMs());
        }
        if (customProperties.getKafka().getMaxPartitionFetchBytes() != null) {
            props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, customProperties.getKafka().getMaxPartitionFetchBytes());
        }
        if (customProperties.getKafka().getFetchMaxWaitMs() != null) {
            props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, customProperties.getKafka().getFetchMaxWaitMs());
        }

        return props;
    }

    @Bean
    public ConsumerFactory<String, UserActionAvro> userActionConsumerFactory() {
        Map<String, Object> props = getCommonConsumerProperties(
                customProperties.getKafka().getUserActionConsumerGroup()
        );
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserActionAvroDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, EventSimilarityAvro> eventsSimilarityConsumerFactory() {
        Map<String, Object> props = getCommonConsumerProperties(
                customProperties.getKafka().getEventsSimilarityConsumerGroup()
        );
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventsSimilarityAvroDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserActionAvro> userActionListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserActionAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userActionConsumerFactory());
        factory.setAutoStartup(false);
        factory.setBatchListener(false);

        if (customProperties.getKafka().getConcurrency() != null) {
            factory.setConcurrency(customProperties.getKafka().getConcurrency());
        }

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro> eventsSimilarityListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventsSimilarityConsumerFactory());
        factory.setAutoStartup(false);
        factory.setBatchListener(false);

        if (customProperties.getKafka().getConcurrency() != null) {
            factory.setConcurrency(customProperties.getKafka().getConcurrency());
        }

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }
}