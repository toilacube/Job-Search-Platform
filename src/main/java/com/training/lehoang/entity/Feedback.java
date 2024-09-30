package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"appCmt\"")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appCmt_id_gen")
    @SequenceGenerator(name = "appCmt_id_gen", sequenceName = "appcmt_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "\"applicationId\"")
    private Integer applicationId;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

}