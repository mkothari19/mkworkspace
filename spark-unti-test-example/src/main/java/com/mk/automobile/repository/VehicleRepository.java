package com.mk.automobile.repository;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

public interface VehicleRepository {
    public  String loadGpsSchema();

    public Dataset<Row> readFromKafkaStream(SparkSession spark, Map<String,String> properties);

}
