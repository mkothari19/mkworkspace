package org.mk.demo.streaming.states;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
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

public class ListStateDemo {

    public static void main(String[] args) throws Exception{

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<GeoLocation> inputStream = env.addSource(new GeoLocationSource());

        inputStream
                .keyBy(elem -> (int)(Math.random()*1))
                .flatMap(new RichFlatMapFunction<GeoLocation, Iterable<GeoLocation>>() {
                    private transient ListState<GeoLocation> window;
                    private transient ValueState<Long> count;
                    @Override
                    public void flatMap(GeoLocation geoLocation, Collector<Iterable<GeoLocation>> collector) throws Exception {
                        Long counter = count.value();
                        counter +=1;
                        count.update(counter);

                        window.add(geoLocation);
                        if(counter >= 10){
                            collector.collect(window.get());
                            window.clear();
                            count.clear();
                        }

                    }
                    @Override
                    public void open(Configuration conf){
                        ListStateDescriptor<GeoLocation> windowDescriptor = new ListStateDescriptor<GeoLocation>("window", GeoLocation.class);
                        window = getRuntimeContext().getListState(windowDescriptor);

                        ValueStateDescriptor<Long> countDescriptor = new ValueStateDescriptor<Long>("count", TypeInformation.of(new TypeHint<Long>() {}), 0L);
                        count = getRuntimeContext().getState(countDescriptor);
                    }
                })
                .print();

        env.execute();
    }
}
