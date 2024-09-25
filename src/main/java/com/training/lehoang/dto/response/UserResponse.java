package com.training.lehoang.dto.response;

import com.training.lehoang.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class UserResponse {
    private int id;
    private String email;
    private String name;
    private String contactInfo;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.contactInfo = user.getContactInfo();
    }
}