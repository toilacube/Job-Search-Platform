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

    @Query("SELECT j FROM Job j " +
            "JOIN j.company c " +  // Join with the company table
            "JOIN j.location l " + // Join with the location table
            "JOIN j.jobTag jt " +  // Join with the job tag table
            "WHERE (:jobTitle IS NULL OR j.jobTitle LIKE %:jobTitle%) AND " +
            "(:jobType IS NULL OR jt.tag = :jobType) AND " +  // Use the jobTag table for job type
            "(:companyName IS NULL OR c.name LIKE %:companyName%) AND " +  // Use the company table
            "(:location IS NULL OR j.location LIKE %:location%) AND " +  // Use the location table
            "j.isDeleted = false")
    ArrayList<Job> findByFiltersAndIsDeletedIsFalse(@Param("jobTitle") String jobTitle,
                                                    @Param("jobType") String jobType,
                                                    @Param("companyName") String companyName,
                                                    @Param("location") String location);



    @Query(value = "SELECT * FROM job WHERE to_tsvector('english', description) @@ to_tsquery(:skills)", nativeQuery = true)
    ArrayList<Job> findJobsBySkills(@Param("skills") String skills);

}
