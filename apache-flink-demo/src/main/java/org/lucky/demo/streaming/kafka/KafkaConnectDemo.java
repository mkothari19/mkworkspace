package org.lucky.demo.streaming.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.lucky.demo.modles.GeoLocation;
import org.lucky.demo.utils.Util;

import java.util.Properties;

public class KafkaConnectDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties prop = new Properties();
        prop.put("group.id", "flink-kafka-example");
        prop.put("bootstrap.servers", "localhost:9092");
//        prop.setProperty("auto.offset.reset", "earliest");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                "streaming-events",
                new SimpleStringSchema(),
                prop
        );
        kafkaConsumer.setStartFromEarliest();

        env.addSource(kafkaConsumer)
                .map(json -> (GeoLocation)Util.readValueAsObject(json,GeoLocation.class))
                .returns(GeoLocation.class)
           .print();

        env.execute();
    }
}
