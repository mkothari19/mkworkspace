package org.lucky.demo.streaming.states;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.lucky.demo.datagen.geolocation.GeoLocationSource;
import org.lucky.demo.modles.GeoLocation;

public class ReducingStateDemo {
    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<GeoLocation> inputStream = env.addSource(new GeoLocationSource());

        inputStream
                .keyBy(elem -> 1)
                .flatMap(
                        new RichFlatMapFunction<GeoLocation, Double>() {
                            private transient ReducingState<Double> sum;
                            private transient ValueState<Long> count;
                            @Override
                            public void flatMap(GeoLocation geoLocation, Collector<Double> collector) throws Exception {
                                Long counter = count.value();
                                counter +=1;
                                count.update(counter);
                                sum.add(Double.valueOf(geoLocation.getGpsLatitude()));
                                if(counter >=10){
                                    collector.collect(sum.get());
                                    sum.clear();
                                    count.clear();
                                }
                            }
                            @Override
                            public void open(Configuration conf){
                                /*ReducingStateDescriptor<Double> sumDescriptor = new ReducingStateDescriptor<Double>("sum", new ReduceFunction<Double>() {
                                    @Override
                                    public Double reduce(Double aDouble, Double t1) throws Exception {
                                        return aDouble+t1;
                                    }
                                }, Double.class);*/
                                /**
                                 * With lambda
                                 */
                                ReducingStateDescriptor<Double> sumDescriptor = new ReducingStateDescriptor<Double>("sum",(Double aDouble, Double t1) -> aDouble+t1, Double.class);
                                sum = getRuntimeContext().getReducingState(sumDescriptor);

                                ValueStateDescriptor<Long> countDescriptor = new ValueStateDescriptor<Long>("count", TypeInformation.of(new TypeHint<Long>() {}), 0L);
                                count = getRuntimeContext().getState(countDescriptor);
                            }
                        }
                )
                .print();

        env.execute();
    }

}
