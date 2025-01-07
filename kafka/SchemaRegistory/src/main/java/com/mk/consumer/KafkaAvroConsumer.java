package com.mk.consumer;


import com.mk.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service

public class KafkaAvroConsumer {

    private Logger logger= LoggerFactory.getLogger(KafkaAvroConsumer.class);
    @KafkaListener(topics="${topic.name}")
    public  void consumerMessage(ConsumerRecord<String, Employee> consumerRecord){
        String key=consumerRecord.key();
        Employee employee=consumerRecord.value();
        logger.info("Message consume= [ key= "+key+" value="+employee.toString());

    }
}
