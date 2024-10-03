package com.training.lehoang.modules.job;

import com.training.lehoang.dto.request.JobRequest;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Company;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.SavedJob;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.jobApp.JobAppRepo;
import com.training.lehoang.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepo jobRepo;
    private final UserService userService;
    private final JobMapper jobMapper;
    private final SavedJobRepo savedJobRepo;
    private final JobAppRepo jobAppRepo;
    public final CompanyRepo companyRepo;

    public ArrayList<JobResponse> listJobs(User recruiter) {
        ArrayList<JobResponse> jobResponses = new ArrayList<>();
        ArrayList<Job> jobs = this.jobRepo.findAllByRecruiterAndIsDeletedIsFalse(recruiter);
        return getJobResponses(jobResponses, jobs);
    }

    public Job createJob(Job job) {
        return this.jobRepo.save(job);
    }

    public Job updateJob(Integer jobId, JobRequest jobRequest) {
        var oldJob = this.jobRepo.findById(jobId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));
        oldJob.setJobTitle(jobRequest.getJobTitle());
        oldJob.setDescription(jobRequest.getJobDescription());
        oldJob.setCompany(companyRepo.findById(jobRequest.getCompanyId()).get());
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

        return getJobResponses(jobResponses, jobs);
    }

    private ArrayList<JobResponse> getJobResponses(ArrayList<JobResponse> jobResponses, ArrayList<Job> jobs) {
        jobs.forEach(job ->{
            JobResponse jobRes = JobResponse.builder()
                    .jobType(job.getJobType())
                    .jobTitle(job.getJobTitle())
                    .companyName(job.getCompany().getName())
                    .description(job.getDescription())
                    .location(job.getLocation())
                    .salary(job.getSalary())
                    .build();
            jobResponses.add(jobRes);
        });

        return jobResponses;
    }

    // Save job
    public JobResponse makeSavedJob(User user, Job job) {
        int jobId = job.getId();

        // Check if user already applied for the job
        boolean isApplied = false;
        if (this.jobAppRepo.existsByJobIdAndUserId(jobId, user.getId())) {
            isApplied = true;
        }

        // Check if job is already saved
        if (this.savedJobRepo.existsByJobIdAndUserId(jobId, user.getId())) {
            return this.jobMapper.toJobResponse(job);
        }
        
        this.savedJobRepo.save(SavedJob.builder()
                .user(user)
                .job(job)
                .isApplied(isApplied)
                .build());

        return this.jobMapper.toJobResponse(job);
    }

    
    
}
