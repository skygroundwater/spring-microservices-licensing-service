package com.springmicroservices.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class Organization extends RepresentationModel<Organization> {

    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

}