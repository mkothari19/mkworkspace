package org.mk.demo.streaming.window.processtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.mk.demo.modles.HomeTempSensor;
import org.mk.demo.utils.Util;

public class ProcessTimeWindowDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);
        ObjectMapper mapper = new ObjectMapper();
        dataStreamSource
                .map(line -> mapper.readValue(line, HomeTempSensor.class))
                .returns(Types.POJO(HomeTempSensor.class))
                .keyBy(elem -> elem.getSensorNumber())
//                .countWindow(10) // Count the record and create the record as per mention count
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))   // for process time
                .reduce(new ReduceFunction<HomeTempSensor>() {
                    @Override
                    public HomeTempSensor reduce(HomeTempSensor t0, HomeTempSensor t1) throws Exception {
                        t0.setTemp(Util.round(t0.getTemp()+t1.getTemp(),2));
                        t0.setCounter(t0.getCounter()+1);
                        t0.setTimeStamp(t1.getTimeStamp());
                        return t0;
                    }
                })
                .print();

        env.execute();
    }

}
