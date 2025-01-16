package com.mk.automobile.config;

public class VehicleConfig {
    public static  final  String schemaRegistryUrl = "http://localhost:8081";
    public static final String subjectName = "avro-vehicle-gps-value";
    public static final String topicName = "avro-vehicle-gps";
    public static final String kafka_bootstrap_server="localhost:9092";
    public static final int schemaVersion=1;
    public static final double geofenceLat=29.2245798;
    public static final double geofenceLon=79.5062298;
    public static final double geofenceRadius=2.0;

}
