package com.mk.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka

public class KafkaConfig {


    @Value("${topic.name}")
    private  String topicName;

    @Bean
    public NewTopic createTopicTest(){
    return  new NewTopic(topicName,3,(short)1);
    }
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "avro-consumer-group1");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", KafkaAvroDeserializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", true); // Enable specific Avro class deserialization
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
