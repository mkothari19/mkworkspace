package org.mk.demo.modles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeTempSensor implements Serializable {

    private int sensorNumber;
    private Long timeStamp;
    private Double temp;
    private int counter;
    private String apiResponse;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HomeTempSensor{");
        sb.append("sensorNumber=").append(sensorNumber);
        sb.append(", timeStamp=").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault()));
        sb.append(", temp=").append(temp);
        sb.append(", counter=").append(counter);
        sb.append(", apiResponse='").append(apiResponse!=null ? apiResponse.replaceAll("[\\n\\t ]", ""): apiResponse).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
