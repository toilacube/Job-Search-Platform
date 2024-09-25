package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "\"jobListings\"")
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobListings_id_gen")
    @SequenceGenerator(name = "jobListings_id_gen", sequenceName = "jobListings_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "\"jobTitle\"", nullable = false)
    private String jobTitle;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "\"companyName\"", nullable = false)
    private String companyName;

    @Column(name = "location")
    private String location;

    @Column(name = "salary", precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "\"jobType\"", length = 50)
    private String jobType;

    @Column(name = "\"isDeleted\"")
    private Boolean isDeleted;

}