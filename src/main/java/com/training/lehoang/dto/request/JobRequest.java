package com.training.lehoang.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@Data
public class JobRequest {
    private String jobTitle;
    private String jobDescription;
    private Integer companyId;
    private String location;
    private BigDecimal salary;
    private String jobType;
    private Instant expiryDate;

    private Integer provinceId;
    private ArrayList<Integer> jobTags;
}
