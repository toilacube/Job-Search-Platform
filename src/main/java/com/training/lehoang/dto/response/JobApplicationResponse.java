package com.training.lehoang.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JobApplicationResponse {
    private String resumeUrl;
    private String coverLetter;
}
