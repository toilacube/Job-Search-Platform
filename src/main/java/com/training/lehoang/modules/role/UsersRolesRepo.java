package com.training.lehoang.modules.role;

import com.training.lehoang.entity.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRolesRepo extends JpaRepository<UsersRole, Long> {
}
