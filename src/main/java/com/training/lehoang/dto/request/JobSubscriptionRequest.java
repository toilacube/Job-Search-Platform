package com.training.lehoang.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class JobSubscriptionRequest {
        private Integer userId;
        private List<Integer> locationIds;
        private List<Integer> jobTagIds;
        private List<Integer> companyIds;
}
