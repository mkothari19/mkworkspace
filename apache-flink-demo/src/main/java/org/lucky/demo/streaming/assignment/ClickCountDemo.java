package org.lucky.demo.streaming.assignment;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.operators.collect.CollectSinkFunction;
import org.lucky.demo.modles.ClickModel;

public class ClickCountDemo {

    /**
     *For every 10 second find out for US country
     * a.) total number of clicks on every website in separate file
     * b.) the website with maximum number of clicks in separate file.
     * c.) the website with minimum number of clicks in separate file.
     * c.) Calculate number of distinct users on every website in separate file.
     * d.) Calculate the average time spent on website by users.
     */

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KeyedStream<ClickModel, String> stream = env.readTextFile("src/main/resources/assignments/ip-data.txt")
                .map(line -> {
                    //user_id,network_name,user_IP,user_country,website, Time spent before next click
                    String[] split = line.split(",");
                    return ClickModel.builder()
                            .userId(split[0])
                            .networkName(split[1])
                            .userIp(split[2])
                            .userCountry(split[3])
                            .website(split[4])
                            .timeSpent(Integer.valueOf(split[5]))
                            .build();
                })
                .returns(Types.POJO(ClickModel.class))
                .keyBy(elem -> elem.getWebsite());

        stream.sum("timeSpent").print();
//        stream.addSink(
//                new CollectSinkFunction<>()
//        )

        env.execute();
    }
}
