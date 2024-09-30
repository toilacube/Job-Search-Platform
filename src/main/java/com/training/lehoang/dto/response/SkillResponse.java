package com.training.lehoang.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
    public int id;
    public String name;
}
