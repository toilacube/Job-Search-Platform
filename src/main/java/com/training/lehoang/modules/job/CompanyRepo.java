package com.training.lehoang.modules.job;

import com.training.lehoang.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company, Integer> {
    Company findById(int id);
}
