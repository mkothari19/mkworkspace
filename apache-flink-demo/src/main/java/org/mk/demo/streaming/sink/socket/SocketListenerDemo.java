package org.mk.demo.streaming.sink.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.mk.demo.modles.HomeTempSensor;

public class SocketListenerDemo {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);
        ObjectMapper mapper = new ObjectMapper();
        dataStreamSource
                .map(json -> mapper.readValue(json, HomeTempSensor.class))
                .returns(Types.POJO(HomeTempSensor.class))
                .keyBy(elem -> elem.getSensorNumber())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .reduce(new ReduceFunction<HomeTempSensor>() {
                    @Override
                    public HomeTempSensor reduce(HomeTempSensor t0, HomeTempSensor t1) throws Exception {
                        t0.setCounter(t0.getCounter()+1);
                        t0.setTemp(t0.getTemp()+t1.getTemp());
                        return t0;
                    }
                })
                .print();
        env.execute();
    }
}
