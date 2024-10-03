package com.training.lehoang.modules.jobSubscription;

import com.training.lehoang.dto.request.JobSubscriptionRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.entity.JobSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class JobSubscriptionController {

    private final JobSubscriptionService jobSubscriptionService;
    // Create or update a job subscription
    @PostMapping
    public ApiResponse<JobSubscription> createOrUpdateSubscription(@RequestBody JobSubscriptionRequest subscriptionRequest) {
        JobSubscription jobSubscription = this.jobSubscriptionService.createOrUpdateSubscription(subscriptionRequest);
        return ApiResponse.<JobSubscription>builder().data(jobSubscription).build();
    }

    // // Retrieve a subscription by userId
    // @GetMapping("/{userId}")
    // public ApiResponse<JobSubscription> getSubscriptionByUserId(@PathVariable Long userId) {

    // }


}
