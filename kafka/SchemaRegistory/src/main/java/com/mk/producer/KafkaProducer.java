package com.mk.producer;

import com.mk.dto.Employee;
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
    private KafkaTemplate<String, Employee> template;

     public void send(Employee employee){
         CompletableFuture<SendResult<String, Employee>> future = template.send(topicName, UUID.randomUUID().toString(), employee);
         future.whenComplete((result,ex)->{

             if(ex==null){
                 System.out.println("Send Message =[" +employee +"] with offset count =["+result.getRecordMetadata().offset() +"]");
             }else {
                 System.out.println("Unable to send message =["+employee +"] due to =["+ ex.getMessage());
             }

         });
     }
}
