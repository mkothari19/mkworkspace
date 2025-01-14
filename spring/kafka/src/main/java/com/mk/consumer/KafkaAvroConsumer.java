package com.mk.consumer;


import com.mk.dto.Employee;
import com.mk.dto.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service

public class KafkaAvroConsumer {

    private Logger logger= LoggerFactory.getLogger(KafkaAvroConsumer.class);
    /*@KafkaListener(topics="${topic.name}")
    public  void consumerMessage(ConsumerRecord<String, Vehicle> consumerRecord){
        String key=consumerRecord.key();
        Vehicle vehicle=consumerRecord.value();
        logger.info("Message consume= [ key= "+key+" value="+vehicle.toString());*/
    @KafkaListener(topics = "${topic.name}", groupId = "avro-consumer-group1")
    public void consume(SpecificRecordBase record) {
        System.out.println("Consumed message: " + record);
    }

    }

