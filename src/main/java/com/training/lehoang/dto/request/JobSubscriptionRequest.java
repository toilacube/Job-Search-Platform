package com.training.lehoang.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
public class JobSubscriptionRequest {
        private Integer userId;
        private ArrayList<Integer> locationIds;
        private ArrayList<Integer> jobTagIds;
        private ArrayList<Integer> companyIds;
}
