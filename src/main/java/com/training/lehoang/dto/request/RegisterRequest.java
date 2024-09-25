package com.training.lehoang.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class RegisterRequest {
    private String email;
//    @Size(min = 10, message = "Password min length is 10")
    private String password;
    private String name;
    private String contactInfo;
}
