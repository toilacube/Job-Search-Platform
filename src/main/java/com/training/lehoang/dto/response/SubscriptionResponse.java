package com.training.lehoang.dto.response;

import com.training.lehoang.entity.JobSubscription;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionResponse {
    private Integer id; // Optional, depending on whether you want to expose the ID
    private List<Integer> locationIds;
    private List<Integer> jobTagIds;
    private List<Integer> companyIds;

    public  SubscriptionResponse(JobSubscription jobSubscription) {

        this.setId(jobSubscription.getId());
        this.setLocationIds(jobSubscription.getLocationIds());
        this.setJobTagIds(jobSubscription.getJobTagIds());
        this.setCompanyIds(jobSubscription.getCompanyIds());
    }
}
