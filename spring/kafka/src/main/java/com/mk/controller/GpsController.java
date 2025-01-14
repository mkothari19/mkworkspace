package com.mk.controller;

import com.mk.dto.Employee;
import com.mk.dto.Vehicle;
import com.mk.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class GpsController {

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/emp-event")
    public String produceMessage(@RequestBody  Employee employee){
        producer.send(employee);
        return "Message Produced";
    }


    @PostMapping("/gps-event")
    public String produceMessage(@RequestBody Vehicle vehicle){
        if(vehicle.getTimestamp().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            vehicle.setTimestamp(LocalDateTime.now().format(formatter));
        }
        producer.send(vehicle);
        return "Message Produced";
    }
}
