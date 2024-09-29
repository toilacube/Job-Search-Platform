package com.training.lehoang.modules.job;

import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface JobRepo extends JpaRepository<Job, Integer> {
    ArrayList<Job> findAllByRecruiterAndIsDeletedIsFalse(User recruiter);
    ArrayList<Job> findAllByIsDeletedIsFalse();
    Optional<Job> findJobByIdAndIsDeletedIsFalse(Integer jobId);
    @Query("SELECT j FROM Job j WHERE " +
            "(:jobTitle IS NULL OR j.jobTitle LIKE %:jobTitle%) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType) AND " +
            "(:companyName IS NULL OR j.companyName LIKE %:companyName%) AND " +
            "(:location IS NULL OR j.location LIKE %:location%)")
    ArrayList<Job> findByFiltersAndIsDeletedIsFalse(@Param("jobTitle") String jobTitle,
                                         @Param("jobType") String jobType,
                                         @Param("companyName") String companyName,
                                         @Param("location") String location);
}
