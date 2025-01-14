package com.mk.service;

import com.mk.dto.Employee;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {
    @Value("${topic.name}")
    private  String topicName;
    @Autowired
    private KafkaTemplate<String, GenericRecord> template;

    public KafkaProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.template = kafkaTemplate;
    }
     public void send(GenericRecord record){
         CompletableFuture<SendResult<String, GenericRecord>> future = template.send(new ProducerRecord<>(topicName, record));
         future.whenComplete((result,ex)->{

             if(ex==null){
                 System.out.println("Send Message =[" +record +"] with offset count =["+result.getRecordMetadata().offset() +"]");
             }else {
                 System.out.println("Unable to send message =["+record +"] due to =["+ ex.getMessage());
             }

         });
     }
}
