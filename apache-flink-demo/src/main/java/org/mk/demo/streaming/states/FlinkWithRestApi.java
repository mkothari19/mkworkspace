package org.mk.demo.streaming.states;

import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.mk.demo.datagen.geolocation.GeoLocationSource;
import org.mk.demo.modles.GeoLocation;

import java.util.concurrent.TimeUnit;

public class FlinkWithRestApi {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<GeoLocation> inputStream = env.addSource(new GeoLocationSource());

        AsyncDataStream.unorderedWait(
                // Original stream
                inputStream,
                // The function
                new HttpClientFunction(),
                // Tiemout length
                5,
                // Timeout unit
                TimeUnit.SECONDS
        )
                .print();

        env.execute();

    }
}
