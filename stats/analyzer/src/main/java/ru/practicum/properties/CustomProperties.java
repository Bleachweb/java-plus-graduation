package ru.practicum.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "my-area-guide")
@Component
@Validated
public class CustomProperties {

    @Valid
    private final Kafka kafka = new Kafka();

    @Valid
    private final Analyzer analyzer = new Analyzer();

    @Getter
    @Setter
    @Validated
    public static class Kafka {

        @NotEmpty
        private String userActionTopic = "user-actions";

        @NotEmpty
        private String eventsSimilarityTopic = "events-similarity";

        @NotEmpty
        private String userActionConsumerGroup = "analyzer-user-action-group";

        @NotEmpty
        private String eventsSimilarityConsumerGroup = "analyzer-events-similarity-group";

        @NotEmpty
        private String bootstrapServers = "localhost:9092";

        @Pattern(regexp = "latest|earliest|none")
        private String autoOffsetReset = "latest";

        @Pattern(regexp = "true|false")
        private String enableAutoCommit = "false";

        @Min(1)
        private String maxPollRecords = "500";

        // Дополнительные настройки
        @Min(1)
        private Integer concurrency = 3;

        @Min(1000)
        private Long maxPollIntervalMs = 300000L;

        @Min(1000)
        private Long sessionTimeoutMs = 10000L;

        @Min(1000)
        private Long heartbeatIntervalMs = 3000L;

        @Min(1024)
        private Integer maxPartitionFetchBytes = 1048576;

        @Min(1)
        private Long fetchMaxWaitMs = 500L;
    }

    @Getter
    @Setter
    @Validated
    public static class Analyzer {

        @Valid
        private final Weights weights = new Weights();

        // Другие настройки аналитики при необходимости
        private Integer similarityThreshold = 80;
        private Integer processingThreads = 4;
    }

    @Getter
    @Setter
    @Validated
    public static class Weights {

        @DecimalMin("0.0")
        @DecimalMax("1.0")
        private String like = "0.9";

        @DecimalMin("0.0")
        @DecimalMax("1.0")
        private String register = "0.7";

        @DecimalMin("0.0")
        @DecimalMax("1.0")
        private String view = "0.3";

        public BigDecimal ofUserAction(UserActionAvro userActionAvro) {
            return switch (userActionAvro.getActionType()) {
                case LIKE -> new BigDecimal(like);
                case REGISTER -> new BigDecimal(register);
                default -> new BigDecimal(view);
            };
        }
    }
}