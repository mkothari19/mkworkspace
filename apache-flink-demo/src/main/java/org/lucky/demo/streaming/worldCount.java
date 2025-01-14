package org.lucky.demo.streaming;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class worldCount {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> stream = env.socketTextStream("localhost", 9999);

        stream
                .map(word -> new Tuple2<>(word, 1))
                .returns(Types.TUPLE(Types.STRING,Types.INT))
                .keyBy(key -> key.f0)
                .sum(1)
                .print();

        env.execute();
    }
}
