package com.training.lehoang.modules.role;

import com.training.lehoang.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
