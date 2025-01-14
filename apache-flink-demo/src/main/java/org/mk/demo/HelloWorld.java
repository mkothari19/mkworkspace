package org.mk.demo;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        // set up the batch execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> text = env.fromElements(
                "Hello",
                "My Dataset API Flink Program");

        DataSet<Tuple2<String, Integer>> wordCounts = text
                .flatMap((String s ,Collector<Tuple2<String,Integer>> out) -> {
                    for(String s1: s.split(" ")){
                        out.collect(new Tuple2<String,Integer>(s1,1));
                    }
                })
                .returns(Types.TUPLE(Types.STRING,Types.INT))
                .groupBy(0)
                .sum(1);

        wordCounts.print();

    }
}
