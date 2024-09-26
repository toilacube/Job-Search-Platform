package com.training.lehoang.modules.jobApp;

import com.training.lehoang.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobAppRepo extends JpaRepository<JobApplication, Integer> {
}
