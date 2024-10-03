package com.training.lehoang.modules.job;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.lehoang.entity.SavedJob;

public interface SavedJobRepo extends JpaRepository<SavedJob, Integer> {
    Boolean existsByJobIdAndUserId(Integer jobId, Integer userId);
    SavedJob findByJobIdAndUserId(Integer jobId, Integer userId);
    ArrayList<SavedJob> findAllByIsAppliedIsFalse();
}
