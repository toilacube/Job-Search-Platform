package com.training.lehoang.dto.response;

import com.training.lehoang.entity.Feedback;
import com.training.lehoang.entity.JobApplication;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFeedbackResponse {
    public Feedback feedback;
    public JobApplicationResponse jobApplication;
}
