package com.training.lehoang.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
public class JobApplicationRequest {
    private MultipartFile resume;

    private String coverLetter;

    private Integer jobId;
}
