package org.lucky.demo.datagen.geolocation;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.lucky.demo.modles.GeoLocation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GeoLocationSource implements SourceFunction<GeoLocation> {

    volatile boolean isRunning = true;
    @Override
    public void run(SourceContext<GeoLocation> ctx) throws Exception {
        while (isRunning){
            /**
             *     "GPSAltitude": "62",
             *     "GPSDateTime": "2021-03-28T02:38:19.000Z",
             *     "GPSHeading": "306.391883",
             *     "GPSLatitude": "41.71454239",
             *     "GPSLongitude": "26.36806488",
             */
            GeoLocation geoLocation = GeoLocation
                    .builder()
                    .gpsAltitude(""+(int)(Math.random()*100))
                    .gpsHeading(""+(Math.random()*1000))
                    .gpsDateTime(""+ LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()))
                    .gpsLatitude(""+(Math.random()*100))
                    .gpsLongitude(""+(Math.random()*100))
                    .build();
            ctx.collect(geoLocation);
            System.out.println("Data generated : "+geoLocation.toString());
            Thread.sleep(1000);
        }

    }

    @Override
    public void cancel() {
        isRunning=false;
    }
}
