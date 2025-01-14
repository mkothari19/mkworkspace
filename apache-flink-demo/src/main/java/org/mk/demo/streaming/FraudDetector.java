package org.mk.demo.streaming;


import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.mk.demo.streaming.model.Alert;
import org.mk.demo.streaming.model.Transaction;

import java.util.Date;
import java.util.UUID;

public class FraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {
    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;


  @Override
    public void processElement(Transaction value, KeyedProcessFunction<Long, Transaction, Alert>.Context ctx, Collector<Alert> out) throws Exception {
        Date date=new Date();

        if(value.getAmount() > 100.00){
            out.collect(Alert.builder()
                    .accountId(value.getAccountId())
                    .alertId(UUID.randomUUID().toString()).
                    alertname("Amount detected from account "+value.getAmount()).
                    alerttime(date.getTime()+"").build());
        }



    }
}
