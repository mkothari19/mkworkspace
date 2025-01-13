package com.spark.geofence.consumer;

import com.spark.udf.UDF;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataTypes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.avro.functions.from_avro;
import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.functions.lit;
@Slf4j
public class GeoFenceConsumer {


    private static final double geofenceLat=29.2245798;
    private static final double geofenceLon=79.5062298;
    private static final double geofenceRadius=2.0;

    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        // Create SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("Kafka Avro Consumer")
                .master("local[*]")
                .getOrCreate();
        // Schema Registry URL
        String schemaRegistryUrl = "http://localhost:8081";

        // Kafka Topic
        String topic = "avro-vehicle-gps";

        // Kafka Stream
        Dataset<Row> kafkaStream = spark.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", topic)
                .option("startingOffsets", "earliest")
                .option("schema.registry.url", schemaRegistryUrl) // Needed for Avro deserialization
                .option("kafka.group.id", "vehicle_sensor_consumer")
                .load();
        // Adjust the byte offset before deserialization
        Dataset<Row> correctedData = kafkaStream.withColumn("corrected_value",
                expr("substring(value, 6, length(value)-5)"));

        String subjectName = "avro-vehicle-gps-value"; // Message Schema name

        // Initialize Schema Registry Client
        SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 10);

        // Fetch Avro schema from Schema Registry
        Schema avroSchema = null;
        try {
            avroSchema = new Schema.Parser().parse(
                    schemaRegistryClient.getLatestSchemaMetadata(subjectName).getSchema());
        } catch (IOException e) {
            log.error("Failed to fetch schema from Schema Registry due to IOException: ", e);
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            log.error("Failed to fetch schema from Schema Registry due to RestClientException: ", e);
            throw new RuntimeException(e);
        }


        // Deserialize Avro messages
        Map<String, String> schemaRegistryConfig = new HashMap<>();
        schemaRegistryConfig.put("schema.registry.url", schemaRegistryUrl);

        Dataset<Row> avroData = correctedData.select(
                from_avro(correctedData.col("corrected_value"), avroSchema.toString(), schemaRegistryConfig)
                        .as("vehicle")
        );

        // Extract fields from Avro data
        Dataset<Row> extractedData = avroData.select(
                avroData.col("vehicle.device_id"),
                avroData.col("vehicle.timestamp"),
                avroData.col("vehicle.latitude"),
                avroData.col("vehicle.longitude"),
                avroData.col("vehicle.altitude"),
                avroData.col("vehicle.speed"),
                avroData.col("vehicle.direction"),
                avroData.col("vehicle.ignition_status")
        );

        Dataset<Row> processedData = extractedData.withColumn("event_time",to_timestamp(col("timestamp")));


        /*
        Calculate distance and geofence status from warehouse(Service center).Here warehouse radius is 2km.
         */
        spark.udf().register("geoFenceDistance", UDF.geoFenceDistance, DataTypes.DoubleType);
        spark.udf().register("isWithinGeoFence",UDF.isWithinGeoFence,DataTypes.BooleanType);

        Dataset<Row> distanceDS=  processedData.withColumn("distance",
                call_udf("geoFenceDistance",col("latitude"),col("longitude"),lit(geofenceLat),lit(geofenceLon)));

        Dataset<Row> warehouseDS=distanceDS.withColumn("inside_warehouse",call_udf("isWithinGeoFence",col("distance"),lit(geofenceRadius)));
        Dataset<Row> result= warehouseDS.withColumn("distance_in_km",concat(col("distance"),lit("km"))).select("device_id","speed","ignition_status","distance_in_km","inside_warehouse");

        // Write to console
       StreamingQuery query= result.writeStream()
                .format("console")
                .outputMode("append")
                .option("checkpointLocation", "/Users/mac/Documents/geofence/")
                .start();
       // Add Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Gracefully stopping the query...");
            if (query != null && query.isActive()) {
                try {
                    query.stop(); // Stop the streaming query
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Query stopped. Cleaning up resources...");
            spark.stop(); // Stop the Spark session
        }));
        query.awaitTermination();


    }

}
