package com.mk.automobile.udf;

import com.mk.automobile.algo.GeoFence;

import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.api.java.UDF4;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UDF {

// Circular Geo fence
    public static UDF4<Double,Double,Double,Double,Double> geoFenceDistance=(Double lat1, Double long1, Double lat2, Double long2)->{
        double distance=  GeoFence.haversine(lat1,long1,lat2,long2);
       return Double.valueOf(Math.round(distance));
    };



    public static UDF2<Double,Double,Boolean> isWithinGeoFence=(Double distance,Double radius )->{
        if(distance<=radius) {
            return true;
        }else {
            return false;
        }
    };
    // manage null or bad timestamp input from stream
    public static UDF1<String, Timestamp >isSafeToTimestamp=(String timestamp)->{
        try {
            if (timestamp != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                sdf.setLenient(false); // Strict parsing
                return new Timestamp(sdf.parse(timestamp).getTime());
            }
        } catch (Exception e) {
            return Timestamp.valueOf("1970-01-01 00:00:00");
        }
        return Timestamp.valueOf("1970-01-01 00:00:00");
    };
}
