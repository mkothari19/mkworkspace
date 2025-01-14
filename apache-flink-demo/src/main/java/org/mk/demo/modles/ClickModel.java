package org.mk.demo.modles;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ClickModel {
//    ## user_id,network_name,user_IP,user_country,website, Time spent before next click
    private String userId;
    private String networkName;
    private String userIp;
    private String userCountry;
    private String website;
    private Integer timeSpent;
}
