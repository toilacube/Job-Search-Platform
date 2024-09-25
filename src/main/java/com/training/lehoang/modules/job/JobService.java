package com.training.lehoang.modules.job;

import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
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
        ArrayList<Job> jobs = this.jobRepo.findAllByRecruiter(recruiter);
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
