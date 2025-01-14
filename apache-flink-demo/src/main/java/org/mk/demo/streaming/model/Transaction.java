package org.mk.demo.streaming.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Transaction {

    private Long accountId;
    private double amount;
    private String city;

}
