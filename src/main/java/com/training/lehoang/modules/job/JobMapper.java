package com.training.lehoang.modules.job;

import com.training.lehoang.dto.request.JobRequest;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Company;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JobMapper {
    public final CompanyRepo companyRepo;
    public final LocationRepo locationRepo;
    // Convert JobRequest to Job entity
    public Job toJobEntity(User recruiter, JobRequest jobRequest) {
        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setJobTitle(jobRequest.getJobTitle());
        job.setDescription(jobRequest.getJobDescription());
        job.setCompany(
                companyRepo.findById(jobRequest.getCompanyId()).get()
        );
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setExpiryDate(jobRequest.getExpiryDate());
        job.setJobType(jobRequest.getJobType());
        job.setProvince(
                locationRepo.findById(jobRequest.getProvinceId()).get());
        job.setJobTags(jobRequest.getJobTags());
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
                .companyName(job.getCompany().getName())
                .build();
    }
}
