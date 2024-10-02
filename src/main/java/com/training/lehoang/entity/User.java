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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "\"passwordHash\"", nullable = false)
    private String passwordHash;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "\"contactInfo\"")
    private String contactInfo;

    @Column(name = "\"resumeUrl\"")
    private String resumeUrl;

    @OneToMany(mappedBy = "user")
    private Set<UsersRole> usersRoles = new LinkedHashSet<>();

    @ColumnDefault("false")
    @Column(name = "\"is2FA\"")
    private Boolean is2FA;

}