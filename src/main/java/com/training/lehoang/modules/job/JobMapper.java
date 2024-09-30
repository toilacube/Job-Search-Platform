package com.training.lehoang.modules.job;

import com.training.lehoang.dto.request.JobRequest;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    // Convert JobRequest to Job entity
    public Job toJobEntity(User recruiter, JobRequest jobRequest) {
        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setJobTitle(jobRequest.getJobTitle());
        job.setDescription(jobRequest.getJobDescription());
        job.setCompanyName(jobRequest.getCompanyName());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setJobType(jobRequest.getJobType());
        job.setIsDeleted(false);
        return job;
    }

    // Convert job to job response object
    public JobResponse toJobResponse(Job job) {
        return JobResponse.builder()
                .salary(job.getSalary())
                .jobTitle(job.getJobTitle())
                .jobType(job.getJobType())
                .location(job.getLocation())
                .description(job.getDescription())
                .companyName(job.getCompanyName())
                .build();
    }
}
