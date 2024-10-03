package com.training.lehoang.modules.jobSubscription;

import com.training.lehoang.dto.request.JobSubscriptionRequest;
import com.training.lehoang.entity.JobSubscription;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSubscriptionService {
    private final JobSubscriptionRepo jobSubscriptionRepo;
    private final UserRepo userRepo;

    public JobSubscription createOrUpdateSubscription(JobSubscriptionRequest subscriptionRequest) {
        User user = this.userRepo.findById(subscriptionRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Try to find an existing job subscription for the user
        Optional<JobSubscription> existingSubscription = this.jobSubscriptionRepo.findByUser(user);

        JobSubscription jobSubscription;

        if (existingSubscription.isPresent()) {
            // Update the existing subscription
            jobSubscription = existingSubscription.get();
            jobSubscription.setJobTagIds(subscriptionRequest.getJobTagIds());
            jobSubscription.setCompanyIds(subscriptionRequest.getCompanyIds());
            jobSubscription.setLocationIds(subscriptionRequest.getLocationIds());
        } else {
            // Create a new subscription
            jobSubscription = JobSubscription.builder()
                    .jobTagIds(subscriptionRequest.getJobTagIds())
                    .user(user)
                    .companyIds(subscriptionRequest.getCompanyIds())
                    .locationIds(subscriptionRequest.getLocationIds())
                    .build();
        }

        // Save the subscription (either newly created or updated)
        this.jobSubscriptionRepo.save(jobSubscription);

        return jobSubscription;
    }
}
