package com.springmicroservices.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@RedisHash("organization")
public class Organization extends RepresentationModel<Organization> {

    @Id
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

}