package com.training.lehoang.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpRequest {
    String email;
    Integer otp;
}
