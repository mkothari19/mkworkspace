package org.mk.demo.streaming.sink.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SocketClientSink;
import org.mk.demo.modles.SocketModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketSinkDemo {

    /**
     * Input stream
     * {"name":"Lucky", "age":30, "city":"Pune"}
     * {"name":"Umesh", "age":31, "city":"Nagpur"}
     * {"name":"Praful", "age":31, "city":"Nagpur"}
     */
    private static final Logger LOG = LoggerFactory.getLogger(SocketSinkDemo.class);
    public static void main(String[] args) throws Exception {
        LOG.info("Program stared......");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);
         dataStreamSource
                .map(json -> stringToJson(json))
                .returns(Types.POJO(SocketModel.class))
                .map(socketModel -> {
                    socketModel.setAge(socketModel.getAge() * 2);
                    return socketModel;
                })
                .map(socketModel -> pojoToJsonString(socketModel))
                .addSink(new SocketClientSink<String>("localhost", 9999, new SimpleStringSchema()));

        env.execute();
    }
    
    public static SocketModel stringToJson(String jsonString ) throws JsonProcessingException {
        return new ObjectMapper()
                     .readValue(jsonString, SocketModel.class);
    }

    public static String pojoToJsonString(SocketModel socketModel) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(socketModel);
    }
}
