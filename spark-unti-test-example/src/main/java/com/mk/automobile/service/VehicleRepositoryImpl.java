package com.mk.automobile.service;

import com.mk.automobile.config.VehicleConfig;
import com.mk.automobile.repository.VehicleRepository;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.Map;

import static org.apache.spark.sql.functions.expr;

@Slf4j
public class VehicleRepositoryImpl implements VehicleRepository {

    @Override
    public  String loadGpsSchema() {
        // Initialize Schema Registry Client
        SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(VehicleConfig.schemaRegistryUrl, 10);

        // Fetch Avro schema from Schema Registry
        String avroSchema = null;
        try {
            int latestVersion = schemaRegistryClient.getLatestSchemaMetadata(VehicleConfig.subjectName).getVersion();
            ParsedSchema latestSchema = schemaRegistryClient.getSchemaBySubjectAndId(VehicleConfig.subjectName,latestVersion);
            avroSchema=latestSchema.toString();

        } catch (IOException e) {
            log.error("Failed to fetch schema from Schema Registry due to IOException: ", e);
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            log.error("Failed to fetch schema from Schema Registry due to RestClientException: ", e);
            throw new RuntimeException(e);
        }
       return avroSchema.toString();
    }

    @Override
    public Dataset<Row> readFromKafkaStream(SparkSession spark, Map<String,String > properties) {
        return spark.readStream()
                .format("kafka")
                .options(properties).load();
         }

}
