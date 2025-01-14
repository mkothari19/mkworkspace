package org.lucky.demo.streaming.window.processtime;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.lucky.demo.datagen.flink.HomeSensorSessionWindowSource;
import org.lucky.demo.modles.HomeTempSensor;

public class SessionWindowDemo {

    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new HomeSensorSessionWindowSource())
                .keyBy(elem -> elem.getSensorNumber())
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(3)))
                .reduce(new ReduceFunction<HomeTempSensor>() {
                    @Override
                    public HomeTempSensor reduce(HomeTempSensor t0, HomeTempSensor t1) throws Exception {
                        t0.setTemp(t0.getTemp()+ t1.getTemp());
                        return t0;
                    }
                })
                .print();

        env.execute();

    }
}
