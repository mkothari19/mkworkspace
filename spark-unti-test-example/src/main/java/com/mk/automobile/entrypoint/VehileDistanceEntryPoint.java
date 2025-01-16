package com.mk.automobile.entrypoint;

import com.mk.automobile.jobs.VehicleDistanceJob;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

public class VehileDistanceEntryPoint {
    public static void main(String[] args) {
        Map<String,String> config=new HashMap<>();
        config.put("kafka.bootstrap.servers", "localhost:9092")
        config.put("subscribe", topic)
        config.put("startingOffsets", "earliest")
        config.put("schema.registry.url", schemaRegistryUrl) // Needed for Avro deserialization
        config.put("kafka.group.id", "vehicle_sensor_consumer")

        SparkSession spark= SparkSession.builder().master("local[*]").appName("Vehicle Distance").getOrCreate();
        new VehicleDistanceJob().distance(spark,config);
    }
}
