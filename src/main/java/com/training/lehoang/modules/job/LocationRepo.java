package com.training.lehoang.modules.job;

import com.training.lehoang.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Integer> {
}
