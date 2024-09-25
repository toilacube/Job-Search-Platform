package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"jobApplications\"")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobApplications_id_gen")
    @SequenceGenerator(name = "jobApplications_id_gen", sequenceName = "jobApplications_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "\"coverLetter\"", length = Integer.MAX_VALUE)
    private String coverLetter;

    @Column(name = "\"resumeUrl\"")
    private String resumeUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"appliedAt\"")
    private Instant appliedAt;

}