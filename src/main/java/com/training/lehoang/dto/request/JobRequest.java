package com.training.lehoang.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobRequest {
    private String jobTitle;
    private String jobDescription;
    private String companyName;
    private String location;
    private BigDecimal salary;
    private String jobType;
}
