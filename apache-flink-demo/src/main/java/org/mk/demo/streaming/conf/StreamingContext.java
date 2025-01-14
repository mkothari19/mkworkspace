package org.mk.demo.streaming.conf;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamingContext {

    public static StreamExecutionEnvironment getStreamingEnv(){
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }
}
