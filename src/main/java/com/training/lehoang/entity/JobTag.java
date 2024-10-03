package com.training.lehoang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jobtags")
public class JobTag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobtags_id_gen")
    @SequenceGenerator(name = "jobtags_id_gen", sequenceName = "jobtag_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "tag", nullable = false, length = 50)
    private String tag;

}