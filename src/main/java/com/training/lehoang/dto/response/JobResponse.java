package com.training.lehoang.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class JobResponse{
    public String jobTitle;
    public String jobType;
    public String description;
    public String companyName;
    public String location;
    public BigDecimal salary;
}
