package org.lucky.demo.streaming.states;

import org.junit.Test;
import org.lucky.demo.modles.GeoLocation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import static org.junit.Assert.*;

public class ValueStateDemoTest {


    @Test
    public void testLattitudeHash(){

        GeoLocation geoLocation = GeoLocation
                .builder()
                .gpsAltitude(""+(int)(Math.random()*100))
                .gpsHeading(""+(Math.random()*1000))
                .gpsDateTime(""+ LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()))
                .gpsLatitude(""+(Math.random()*100))
                .gpsLongitude(""+(Math.random()*100))
                .build();
        int longitude = geoLocation.getGpsLongitude().hashCode();
        Random rand = new Random();
        int div=rand.nextInt(10) + 1;
        System.out.println(longitude+" => "+(longitude/div));
        System.out.println(longitude / (longitude/div));

    }

}