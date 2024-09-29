package com.training.lehoang.modules.job;

import com.training.lehoang.dto.request.JobRequest;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.user.UserRepo;
import com.training.lehoang.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepo jobRepo;
    private final UserService userService;

    public ArrayList<JobResponse> listJobs(User recruiter) {
        ArrayList<JobResponse> jobResponses = new ArrayList<>();
        ArrayList<Job> jobs = this.jobRepo.findAllByRecruiterAndIsDeletedIsFalse(recruiter);
        jobs.forEach(job ->{
            JobResponse jobRes = JobResponse.builder()
                    .jobType(job.getJobType())
                    .jobTitle(job.getJobTitle())
                    .companyName(job.getCompanyName())
                    .description(job.getDescription())
                    .location(job.getLocation())
                    .salary(job.getSalary())
                    .build();
            jobResponses.add(jobRes);
        });

        return jobResponses;
    }

    public Job createJob(Job job) {
        return this.jobRepo.save(job);
    }

    public Job updateJob(Integer jobId, JobRequest jobRequest) {
        var oldJob = this.jobRepo.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));
        oldJob.setJobTitle(jobRequest.getJobTitle());
        oldJob.setDescription(jobRequest.getJobDescription());
        oldJob.setCompanyName(jobRequest.getCompanyName());
        oldJob.setLocation(jobRequest.getLocation());
        oldJob.setSalary(jobRequest.getSalary());
        return this.jobRepo.save(oldJob);
    }

    public Job deleteJob(Integer jobId) {
        Job job = this.jobRepo.findJobByIdAndIsDeletedIsFalse(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));
        job.setIsDeleted(true);
        this.jobRepo.save(job);
        return job;
    }

    public Job getJob(Integer jobId) {
        return this.jobRepo.findById(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));

    }
    public ArrayList<JobResponse> listJobUser(String jobTitle, String jobType, String companyName, String location) {
        ArrayList<JobResponse> jobResponses = new ArrayList<>();
        ArrayList<Job> jobs;
        if (jobTitle != null || jobType != null || companyName != null || location != null) {
            jobs = jobRepo.findByFiltersAndIsDeletedIsFalse(jobTitle, jobType, companyName, location);
        }
        else {
            jobs = this.jobRepo.findAllByIsDeletedIsFalse();
        }

        jobs.forEach(job ->{
            JobResponse jobRes = JobResponse.builder()
                    .jobType(job.getJobType())
                    .jobTitle(job.getJobTitle())
                    .companyName(job.getCompanyName())
                    .description(job.getDescription())
                    .location(job.getLocation())
                    .salary(job.getSalary())
                    .build();
            jobResponses.add(jobRes);
        });

        return jobResponses;
    }
}
