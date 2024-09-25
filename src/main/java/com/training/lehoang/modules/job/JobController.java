package com.training.lehoang.modules.job;

import com.training.lehoang.dto.request.JobRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.user.CustomUserDetails;
import com.training.lehoang.modules.user.UserRepo;
import com.training.lehoang.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final UserService userService;
    private final JobMapper jobMapper;

    @GetMapping("")
    public ApiResponse<ArrayList<JobResponse>> listJob() {
        User recruiter = userService.getUser();
        ArrayList<JobResponse> jobList = this.jobService.listJobs(recruiter);
        return ApiResponse.< ArrayList<JobResponse>>builder()
                .data(jobList)
                .build();
    }

    @PostMapping()
    public ApiResponse<JobResponse> createJob(@RequestBody JobRequest jobRequest) {
        User recruiter = this.userService.getUser();
        Job job = this.jobMapper.toJobEntity(recruiter, jobRequest);
        Job newJob = this.jobService.createJob(job);

        var jobRes = this.jobMapper.toJobResponse(newJob);
        return ApiResponse.<JobResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(jobRes)
                .build();
    }

    @PatchMapping("{jobId}")
    public ApiResponse<JobResponse> updateJob(@PathVariable Integer jobId, @RequestBody JobRequest jobRequest) {
        User recruiter = this.userService.getUser();
        Job updatedJob = this.jobService.updateJob(jobId, jobRequest);

        var jobRes = this.jobMapper.toJobResponse(updatedJob);
        return ApiResponse.<JobResponse>builder()
                .data(jobRes)
                .message("Job updated successfully")
                .build();
    }

    @DeleteMapping("{jobId}")
    public ApiResponse<JobResponse> deleteJob(@PathVariable Integer jobId) {
        User recruiter = this.userService.getUser();
        Job job = this.jobService.getJob(jobId);

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        Job deletedJob = this.jobService.deleteJob(jobId);
        JobResponse jobRes = this.jobMapper.toJobResponse(deletedJob);
        return ApiResponse.<JobResponse>builder()
                .message("Job deleted successfully")
                .data(jobRes)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<ArrayList<JobResponse>> listJobUser(
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location
    ) {
        ArrayList<JobResponse> jobList = this.jobService.listJobUser(jobTitle, jobType, companyName, location);
        return ApiResponse.< ArrayList<JobResponse>>builder()
                .data(jobList)
                .build();
    }
}
