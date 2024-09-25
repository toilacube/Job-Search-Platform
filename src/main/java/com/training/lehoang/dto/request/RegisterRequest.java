package com.training.lehoang.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String contactInfo;
}
