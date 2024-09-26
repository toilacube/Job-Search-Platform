package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "job")
public class Job {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_id_gen")
//    @SequenceGenerator(name = "job_id_gen", sequenceName = "job_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"recruiterId\"", nullable = false)
    private User recruiter;

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

    @OneToMany(mappedBy = "job")
    private Set<JobApplication> jobApplications = new LinkedHashSet<>();

}