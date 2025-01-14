package org.lucky.demo.streaming.iterator;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import java.util.Arrays;

public class IterativeStreamDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Tuple3<Integer, Integer,Integer>> streamSource = env.fromCollection(Arrays.asList(
                Tuple3.of(0, 0, 0),
                Tuple3.of(1, 0, 1),
                Tuple3.of(2, 0, 2),
                Tuple3.of(3, 0, 3),
                Tuple3.of(4, 0, 4),
                Tuple3.of(5, 0, 5)));

        IterativeStream<Tuple3<Integer, Integer,Integer>> iterativeStream = streamSource.iterate(5000);

        SingleOutputStreamOperator<Tuple3<Integer, Integer,Integer>> streamOperator = iterativeStream
                .map(tup3 -> tup3.f0 == 10 ? tup3 : Tuple3.of(tup3.f0 + 1, tup3.f1 + 1,tup3.f2))
                .returns(Types.TUPLE(Types.INT, Types.INT,Types.INT));

        SingleOutputStreamOperator<Tuple3<Integer, Integer,Integer>> notEqualStream = streamOperator
                .filter(tup3 -> tup3.f0 != 10);
        /**
         * Origanal and feed stream partioned must be same
         */
        notEqualStream.setParallelism(streamSource.getParallelism());
        iterativeStream.closeWith(notEqualStream);

        streamOperator
                .filter(tup3 -> tup3.f0 == 10)
        .print();

        env.execute();
    }
}
