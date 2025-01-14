package org.mk.demo.streaming.agg;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.mk.demo.modles.Sales;

public class AggDemo {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamDs = env.readTextFile("src/main/resources/reduce/avgData");

        KeyedStream<Sales, String> keyByDS = streamDs
                .map(
                        line -> {
                            String[] split = line.split(",");
                            return new Sales(split[0], split[1], split[2], split[3], Integer.valueOf(split[4]), 1);
                        }
                )
                .returns(Types.POJO(Sales.class))
                .keyBy(elem -> elem.getMonth());

        System.out.println("************** SUM ON Stream ********************");
        keyByDS.sum("sales").print();
        System.out.println("************** MAX ON Stream ********************");
        keyByDS.max("sales").print();
        System.out.println("************** MIN ON Stream ********************");
        keyByDS.min("sales").print();
        System.out.println("************** MAX By ON Stream ********************");
        keyByDS.maxBy("sales").print();
        System.out.println("************** MIN By ON Stream ********************");
        keyByDS.minBy("sales").print();

       /* keyByDS
                .minBy("sales")
                .map(sales -> sales.toString())
                .addSink(
                        new SocketClientSink<String>("localhost",9999,new SimpleStringSchema())
                );*/

        env.execute();
    }
}
