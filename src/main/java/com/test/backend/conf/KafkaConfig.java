package com.test.backend.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("pagamentos")
                .partitions(1)
                .replicas(1)
                .build();
    }

}
