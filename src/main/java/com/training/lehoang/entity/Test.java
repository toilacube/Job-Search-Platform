package com.training.lehoang.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class Test implements Serializable {
    private Integer id;
    private String name;
}
