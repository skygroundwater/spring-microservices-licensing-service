package com.springmicroservices.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "licenses")
public class License extends RepresentationModel<License> {

    @Id
    @Column(name = "licenseId", nullable = false)
    private String licenseId;
    @Column(name = "description")
    private String description;
    @Column(name = "organizationId", nullable = false)
    private String organizationId;
    @Column(name = "productName", nullable = false)
    private String productName;
    @Column(name = "licenseType", nullable = false)
    private String licenseType;
    @Column(name = "comment")
    private String comment;
    @Transient
    private String organizationName;
    @Transient
    private String contactName;
    @Transient
    private String contactPhone;
    @Transient
    private String contactEmail;


    public License withComment(String comment) {
        this.setComment(comment);
        return this;
    }
}