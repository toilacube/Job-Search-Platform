package com.training.lehoang.modules.mail;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserAndJob implements Serializable {
    private String email;
    private String name;
    private String contactInfo;
    private String jobTitle;
    private String companyName;
    private String description;

}
