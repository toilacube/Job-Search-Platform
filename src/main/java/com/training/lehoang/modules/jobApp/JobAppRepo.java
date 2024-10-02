package com.training.lehoang.modules.jobApp;

import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface JobAppRepo extends JpaRepository<JobApplication, Integer> {

        Optional<JobApplication> findFirstByJobId(Integer jobId);
        ArrayList<JobApplication> findByUserId(Integer userId);

        Boolean existsByJobIdAndUserId(Integer jobId, Integer userId);
}
