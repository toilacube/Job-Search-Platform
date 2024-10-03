package com.training.lehoang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "jobsubscriptions")
@NoArgsConstructor
@AllArgsConstructor
public class JobSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobsubscriptions_id_gen")
    @SequenceGenerator(name = "jobsubscriptions_id_gen", sequenceName = "jobsubscription_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @ColumnDefault("'{}'")
    @Column(name = "\"locationIds\"")
    @ElementCollection
    private List<Integer> locationIds;


    @ColumnDefault("'{}'")
    @Column(name = "\"jobTagIds\"")
    @ElementCollection
    private List<Integer> jobTagIds;

    @ColumnDefault("'{}'")
    @Column(name = "\"companyIds\"")
    @ElementCollection
    private List<Integer> companyIds;

}