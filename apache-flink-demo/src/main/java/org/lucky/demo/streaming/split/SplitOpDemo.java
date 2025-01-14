package org.lucky.demo.streaming.split;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class SplitOpDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // String type side output for Even values
        final OutputTag < Integer > evenOutTag = new OutputTag < Integer > ("even-string-output") {};
        // Integer type side output for Odd values
        final OutputTag < Integer > oddOutTag = new OutputTag< Integer >("odd-int-output") {};

        DataStreamSource<Integer> source = env.fromElements(Integer.class, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        SingleOutputStreamOperator<Integer> streamOperator = source
                .process(
                        new ProcessFunction<Integer, Integer>() {
                            @Override
                            public void processElement(Integer integer, Context context, Collector<Integer> collector) throws Exception {
                                collector.collect(integer);
                                if (integer % 2 == 0)
                                    context.output(evenOutTag, integer);
                                else
                                    context.output(oddOutTag, integer);
                            }
                        }
                );

        /*streamOperator.getSideOutput(evenOutTag)
                .print();*/
        System.out.println("**************************************");
        streamOperator.getSideOutput(oddOutTag)
                .print();

        env.execute();

    }
}
