package com.mk.automobile.jobs;

import com.mk.automobile.config.VehicleConfig;
import com.mk.automobile.repository.VehicleRepository;
import com.mk.automobile.udf.UDF;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import java.util.Map;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.avro.functions.*;
import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.*;

public class VehicleDistanceJob {
    private VehicleRepository vehicleRepository;

    public VehicleDistanceJob(VehicleRepository vehicleRepository){
        this.vehicleRepository=vehicleRepository;
    }

    public Dataset<Row> distance(SparkSession spark, Map<String,String> properties){
      String avroSchema=  vehicleRepository.loadGpsSchema();
       Dataset<Row> avroStream= vehicleRepository.readFromKafkaStream(spark,properties);
        Dataset<Row> correctedData = avroStream.withColumn("corrected_value",
                expr("substring(value, 6, length(value)-5)"));
       Dataset<Row> avroData =correctedData.select(from_avro(col("corrected_value"),avroSchema, properties)) .as("vehicle");
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
                call_udf("geoFenceDistance",col("latitude"),col("longitude"),lit(VehicleConfig.geofenceLat),lit(VehicleConfig.geofenceLon)));
        return distanceDS;

    }

}
