package org.mk.demo.modles;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeAndTemp {
    private Long timeStamp;
    private Double temp;
}
