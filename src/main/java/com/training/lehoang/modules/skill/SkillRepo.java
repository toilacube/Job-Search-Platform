package com.training.lehoang.modules.skill;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.lehoang.entity.Skill;

public interface SkillRepo extends JpaRepository<Skill, Integer> {
    ArrayList<Skill> findByIdIn(ArrayList<Integer> skills); 
}
