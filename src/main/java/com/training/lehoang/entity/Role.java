package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_gen")
    @SequenceGenerator(name = "roles_id_gen", sequenceName = "roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UsersRole> usersRoles = new LinkedHashSet<>();

}