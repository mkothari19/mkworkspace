package org.lucky.demo.modles;

import lombok.*;

import java.io.Serializable;



@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CabModel implements Serializable {

//    # cab id, cab number plate, cab type, cab driver name, ongoing trip/not, pickup location, destination,passenger count

    private String cabId;
    private String cabNumber;
    private String cabType;
    private String cabDriverName;
    private Boolean ongoing;
    private String pickup;
    private String destination;
    private Integer passengerCount;

}
