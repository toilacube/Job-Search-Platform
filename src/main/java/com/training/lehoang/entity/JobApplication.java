package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"jobApplications\"")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobApplications_id_gen")
    @SequenceGenerator(name = "jobApplications_id_gen", sequenceName = "jobapplications_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"jobId\"", nullable = false)
    private Job job;

    @Column(name = "\"coverLetter\"", length = Integer.MAX_VALUE)
    private String coverLetter;

    @Column(name = "\"resumeUrl\"")
    private String resumeUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "\"appliedAt\"")
    private Instant appliedAt;

}