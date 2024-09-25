package com.training.lehoang.modules.job;

import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.JobResponse;
import com.training.lehoang.entity.User;
import com.training.lehoang.modules.user.CustomUserDetails;
import com.training.lehoang.modules.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final UserRepo userRepo;
//    @GetMapping("/{recruiterId}")
//    public ApiResponse<ArrayList<JobResponse>> jobListing(@PathVariable Integer recruiterId) {
//        ArrayList<JobResponse> jobList = this.jobService.listJobs(recruiterId);
//        return ApiResponse.< ArrayList<JobResponse>>builder()
//                .data(jobList)
//                .build();
//    }

    @GetMapping("")
    public ApiResponse<ArrayList<JobResponse>> jobListing() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User recruiter = userRepo.findByEmail(authentication.getName()).orElse(null);
        ArrayList<JobResponse> jobList = this.jobService.listJobs(recruiter);
        return ApiResponse.< ArrayList<JobResponse>>builder()
                .data(jobList)
                .build();
    }

}
