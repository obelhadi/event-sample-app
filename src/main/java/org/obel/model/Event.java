package org.obel.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements HasId {

    private String id;
    private String name;
    private String description;
    private String category;
    private Date beginDate;
    private Date endDate;
    private String infoUrl;

    private String city;
    private String zipCode;

    private String address;

}
