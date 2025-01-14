package org.lucky.demo.streaming.reduce;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.lucky.demo.modles.Sales;

public class ReduceDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamDs = env.readTextFile("src/main/resources/reduce/avgData");

        streamDs
                .map(
                        line -> {
                            String[] split = line.split(",");
                            return new Sales(split[0],split[1],split[2],split[3],Integer.valueOf(split[4]),1);
                        }
                )
                .returns(Types.POJO(Sales.class))
                .keyBy(elem -> elem.getMonth())
                .reduce(new ReduceFunction<Sales>() {
                    @Override
                    public Sales reduce(Sales sales, Sales t1) throws Exception {
                         sales.setSales(sales.getSales()+t1.getSales());
                         sales.setCount(sales.getCount()+ t1.getCount());
                         return sales;
                    }
                })
                .map(sales -> Tuple3.of(sales.getMonth(), sales.getSales() / sales.getCount(), sales.getCount()))
                .returns(Types.TUPLE(Types.STRING,Types.INT,Types.INT))
                .print();

        env.execute();
    }
}
