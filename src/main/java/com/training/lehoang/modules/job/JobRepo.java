package com.training.lehoang.modules.job;

import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface JobRepo extends JpaRepository<Job, Integer> {
    ArrayList<Job> findAllByRecruiter(User recruiter);
}
