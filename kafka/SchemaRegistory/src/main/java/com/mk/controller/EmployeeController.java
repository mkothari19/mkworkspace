package com.mk.controller;

import com.mk.dto.Employee;
import com.mk.producer.KafkaProducer;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/emp-event")
    public String produceMessage(@RequestBody  Employee employee){
        producer.send(employee);
        return "Message Produced";
    }
}
