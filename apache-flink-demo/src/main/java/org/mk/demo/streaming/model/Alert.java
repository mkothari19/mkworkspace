package org.mk.demo.streaming.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Alert implements Serializable {
    private String alertId;
    private String alertname;
    private String alerttime;
    private Long accountId;
}
