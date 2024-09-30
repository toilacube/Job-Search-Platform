package com.training.lehoang.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"usersSkills\"")
public class UsersSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersSkills_id_gen")
    @SequenceGenerator(name = "usersSkills_id_gen", sequenceName = "usersskills_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"userId\"")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"skillId\"")
    private Skill skill;

}