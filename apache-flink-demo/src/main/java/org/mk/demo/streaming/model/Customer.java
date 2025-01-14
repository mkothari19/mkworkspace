package org.mk.demo.streaming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class Customer {
    private String accountID;
    private String name;
    private String email;
    private int age ;



}
