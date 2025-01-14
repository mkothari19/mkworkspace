package org.lucky.demo.dataset.agg;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple7;

import java.util.Arrays;

import static org.apache.flink.api.java.aggregation.Aggregations.*;

public class AggregateDemo {

    public static void main(String[] args) throws Exception {
        /**
         * The Aggregate transformation can only be applied on a Tuple DataSet.
         */
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> streamDs =  env.fromCollection(
                Arrays.asList(
                        "01-06-2018,June,Category5,Bat,12",
                        "01-06-2108,June,Category4,Perfume,10",
                        "13-07-2018,July,Category1,Television,50",
                        "24-06-2018,June,Category4,Shirt,38"
                )
        );
        streamDs
                .map(
                        line -> {
                            String[] split = line.split(",");
                            return new Tuple7(split[0],split[1],split[2],split[3],Integer.valueOf(split[4]),1,Integer.valueOf(split[4]));
                        }
                )
                .returns(Types.TUPLE(Types.STRING,Types.STRING,Types.STRING,Types.STRING,Types.INT,Types.INT,Types.INT))
                .aggregate(MAX, 4)
                .and(SUM,5)
                .and(MIN, 6)
                .project(4,5,6)
                .print();
    }
}
