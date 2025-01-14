package org.mk.demo.streaming.kafka;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.mk.demo.streaming.FraudDetector;
import org.mk.demo.streaming.model.Alert;
import org.mk.demo.streaming.model.Transaction;
import org.mk.demo.utils.Util;

import java.util.Properties;

public class FraudDetectionJob {


    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();

        Properties props=new Properties();
        props.put("group.id", "flink-kafka-example");
        props.put("bootstrap.servers", "localhost:9092");
        FlinkKafkaConsumer consumer=new FlinkKafkaConsumer<>("transaction-account", new SimpleStringSchema(),props);
        consumer.setStartFromEarliest();

              DataStream<Transaction> transactionDataStream= env.addSource(consumer).
                      map(json->(Transaction) Util.readValueAsObject((String) json,Transaction.class)).returns(Transaction.class);


    DataStream<Alert> alert= transactionDataStream.keyBy(Transaction::getAccountId).process(new FraudDetector());
        FlinkKafkaProducer producer=new FlinkKafkaProducer<>("account-alert", new SimpleStringSchema(),props);
        producer.setWriteTimestampToKafka(true);

         alert.map(x->x.toString()).returns(Types.STRING).addSink(producer);
        //alert.executeAndCollect().forEachRemaining(x->System.out.println("Here******"+x));

        //Reach sink function

        alert.addSink(
                 JdbcSink.sink(
                 "insert into account_alert (alert_id,alertname,alerttime,account_id) values (?, ?, ?, ?)",
                 (statement,value) -> {

                     statement.setString(1, value.getAlertId());
                     statement.setString(2, value.getAlertname());
                     statement.setString(3, value.getAlerttime());
                     statement.setLong(4, value.getAccountId());

                 },JdbcExecutionOptions.builder()
                         .withBatchSize(1000)
                         .withBatchIntervalMs(200)
                         .withMaxRetries(5)
                         .build(),
                  new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                         .withUrl("jdbc:postgresql://localhost:5433/alert")
                         .withDriverName("org.postgresql.Driver")
                         .withUsername("postgres")
                         .withPassword("1234")
                         .build()
         ));
         env.execute();


       
    }
}
