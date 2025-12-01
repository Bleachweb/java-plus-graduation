package ru.practicum.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties("my-area-guide")
@Component
public class CustomProperties {

    private KafkaProperties kafka = new KafkaProperties();
    private AnalyzerProperties analyzer = new AnalyzerProperties();

    @Getter
    @Setter
    public static class KafkaProperties {
        private String bootstrapServers = "localhost:9092";
        private String autoOffsetReset = "latest";
        private Boolean enableAutoCommit = false;
        private Integer maxPollRecords = 500;

        private TopicsProperties topics = new TopicsProperties();
        private ConsumerGroupsProperties consumerGroups = new ConsumerGroupsProperties();

        @Getter
        @Setter
        public static class TopicsProperties {
            private String userAction = "user-actions";
            private String eventsSimilarity = "events-similarity";
        }

        @Getter
        @Setter
        public static class ConsumerGroupsProperties {
            private String userAction = "analyzer-user-action-group";
            private String eventsSimilarity = "analyzer-events-similarity-group";
        }
    }

    @Getter
    @Setter
    public static class AnalyzerProperties {
        private WeightsProperties weights = new WeightsProperties();

        @Getter
        @Setter
        public static class WeightsProperties {
            private BigDecimal like = new BigDecimal("0.9");
            private BigDecimal register = new BigDecimal("0.7");
            private BigDecimal view = new BigDecimal("0.3");

            public BigDecimal ofUserAction(UserActionAvro userActionAvro) {
                return switch (userActionAvro.getActionType()) {
                    case LIKE -> like;
                    case REGISTER -> register;
                    default -> view;
                };
            }
        }
    }
}