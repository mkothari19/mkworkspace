package org.mk.demo.streaming.kafka;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.mk.demo.datagen.geolocation.GeoLocationSource;

import java.util.Properties;

public class KafkaProducerDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties prop = new Properties();
        prop.put("group.id", "flink-kafka-example");
        prop.put("bootstrap.servers", "localhost:9092");
        prop.setProperty("auto.offset.reset", "earliest");

        FlinkKafkaProducer<String> producer  = new FlinkKafkaProducer<>("streaming-events", new SimpleStringSchema(), prop);

        env
                .addSource(new GeoLocationSource())
                .map(elem -> elem.toString())
                .returns(Types.STRING)
                .addSink(producer);

        env.execute(Class.class.getName());
    }
}
