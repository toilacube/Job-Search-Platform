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


    @Query(value = "SELECT j.* FROM job j " +
            "JOIN company c ON j.\"companyId\" = c.id " +
            "WHERE j.\"isDeleted\" = false " +
            "AND (:jobTitle IS NULL OR j.\"jobTitle\" LIKE CONCAT('%', :jobTitle, '%')) " +
            "AND (:jobType IS NULL OR j.\"jobType\" LIKE CONCAT('%', :jobType, '%')) " +
            "AND (:companyName IS NULL OR c.\"name\" LIKE CONCAT('%', :companyName, '%')) " +
            "AND (:location IS NULL OR j.\"location\" LIKE CONCAT('%', :location, '%'))",
            nativeQuery = true)
    ArrayList<Job> findByFiltersAndIsDeletedIsFalse(@Param("jobTitle") String jobTitle,
                                                    @Param("jobType") String jobType,
                                                    @Param("companyName") String companyName,
                                                    @Param("location") String location);



    @Query(value = "SELECT * FROM job WHERE to_tsvector('english', description) @@ to_tsquery(:skills)", nativeQuery = true)
    ArrayList<Job> findJobsBySkills(@Param("skills") String skills);


    ArrayList<Job> findAllByIsDeletedIsFalseAndIdIn(ArrayList<Integer> jobIds);
}
