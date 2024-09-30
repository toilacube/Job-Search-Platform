package com.training.lehoang.modules.skill;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.lehoang.entity.UsersSkill;
import com.training.lehoang.entity.Skill;
import com.training.lehoang.entity.User;

public interface UserSkillRepo extends JpaRepository<UsersSkill, Integer> {
    
    ArrayList<UsersSkill> findAllByUser(User user);
}
