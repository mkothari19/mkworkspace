package org.mk.demo.streaming.states;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.mk.demo.datagen.geolocation.GeoLocationSource;
import org.mk.demo.modles.GeoLocation;

public class ValueStateDemo {

    public static void main(String[] args) throws Exception{

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<GeoLocation> inputStream = env.addSource(new GeoLocationSource());

        inputStream
                .keyBy(elem -> 1)
                .flatMap(new FlatMapImpl())
                .print();

        env.execute();

    }

    public static class FlatMapImpl extends RichFlatMapFunction<GeoLocation, Double> {
        private transient ValueState<Double> sum;
        private transient ValueState<Long> count;
        @Override
        public void flatMap(GeoLocation geoLocation, Collector<Double> collector) throws Exception {
            Double accumulativeSum = sum.value();
            Long counter = count.value();

            accumulativeSum = Double.valueOf(geoLocation.getGpsAltitude())+accumulativeSum;
            counter +=1L;
            sum.update(accumulativeSum);
            count.update(counter);

            if(counter >= 10){
                collector.collect(sum.value());
                sum.clear();
                count.clear();
            }


        }

        @Override
        public void open(Configuration conf){
            ValueStateDescriptor<Double> sumDescriptor = new ValueStateDescriptor<Double>("sum", TypeInformation.of(new TypeHint<Double>() {}), 0.0);
            sum = getRuntimeContext().getState(sumDescriptor);

            ValueStateDescriptor<Long> countDescriptor = new ValueStateDescriptor<Long>("count", TypeInformation.of(new TypeHint<Long>() {}), 0L);
            count = getRuntimeContext().getState(countDescriptor);
        }
    }
}
