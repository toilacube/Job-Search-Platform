package com.training.lehoang.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record JobResponse (
        String jobTitle,
        String jobType,
        String description,
        String companyName,
        String location,
        BigDecimal salary) {

}
