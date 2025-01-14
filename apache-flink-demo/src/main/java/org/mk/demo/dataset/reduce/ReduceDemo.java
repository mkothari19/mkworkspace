package org.mk.demo.dataset.reduce;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;
import org.mk.demo.modles.Sales;

public class ReduceDemo {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> streamDs = env.readTextFile("src/main/resources/reduce/avgData");
        streamDs
                .map(
                        line -> {
                            String[] split = line.split(",");
                            return new Sales(split[0],split[1],split[2],split[3],Integer.valueOf(split[4]),1);
                        }
                )
                .returns(Types.POJO(Sales.class))
                .groupBy("month")
                .reduce(new ReduceFunction<Sales>() {
                    @Override
                    public Sales reduce(Sales sales, Sales t1) throws Exception {
                         sales.setSales(sales.getSales()+t1.getSales());
                         sales.setCount(sales.getCount()+ t1.getCount());
                         return sales;
                    }
                })
                .map(sales -> Tuple3.of(sales.getMonth(), Double.valueOf(sales.getSales()) / sales.getCount(), sales.getCount()))
                .returns(Types.TUPLE(Types.STRING,Types.DOUBLE,Types.INT))
                .print();
    }
}
