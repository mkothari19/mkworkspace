package org.lucky.demo.modles;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.lucky.demo.utils.Util;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoLocation implements Serializable {



    /**
     *     "GPSAltitude": "62",
     *     "GPSDateTime": "2021-03-28T02:38:19.000Z",
     *     "GPSHeading": "306.391883",
     *     "GPSLatitude": "41.71454239",
     *     "GPSLongitude": "26.36806488",
     */
    private String gpsAltitude;
    private String gpsDateTime;
    private String gpsHeading;
    private String gpsLatitude;
    private String gpsLongitude;
    private String apiResponse;

    @Override
    public String toString() {
        return Util.writeValueAsString(this);
    }
}
