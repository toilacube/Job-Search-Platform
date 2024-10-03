package com.training.lehoang.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "jobnotifications")
public class JobNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobnotifications_id_gen")
    @SequenceGenerator(name = "jobnotifications_id_gen", sequenceName = "jobnotification_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;
    

    @NotNull
    @Type(ListArrayType.class)
    @Column(name = "\"jobIds\"",
            columnDefinition = "integer[]")
    private ArrayList<Integer> jobIds;

}