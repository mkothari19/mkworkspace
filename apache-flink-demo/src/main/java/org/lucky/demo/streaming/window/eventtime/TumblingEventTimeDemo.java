package org.lucky.demo.streaming.window.eventtime;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.util.Collector;
import org.lucky.demo.datagen.flink.HomeSensorSource;
import org.lucky.demo.modles.HomeTempSensor;

public class TumblingEventTimeDemo {
    public static void main(String[] args) throws Exception{

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // set to EventTime else it defaults to ProcessTime
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.addSource(new HomeSensorSource())
                .assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<HomeTempSensor>(Time.seconds(0)) {
                            @Override
                            public long extractTimestamp(HomeTempSensor homeTempSensor) {
                                return homeTempSensor.getTimeStamp();
                            }
                        }
                )
                .keyBy(elem -> elem.getSensorNumber())
                .window(TumblingEventTimeWindows.of(Time.seconds(6)))
//                .allowedLateness(Time.seconds(1))
                .process(new ProcessWindowFunction<HomeTempSensor, Double, Integer, TimeWindow>() {
                    @Override
                    public void process(Integer integer, Context context, Iterable<HomeTempSensor> input, Collector<Double> collector) throws Exception {
                       System.out.println("Processing records :: "+input);
                        Double sum=0.0;
                        int totalRecords=0;
                        int sensorNumber=0;
                        for(HomeTempSensor sensor : input){
                            sum += sensor.getTemp();
                            totalRecords++;
                            sensorNumber = sensor.getSensorNumber();
                        }
                        System.out.println("Processing total records :: "+totalRecords+" sensor number :: "+sensorNumber);

                    }
                })
                .print();

        env.execute();
    }
}
