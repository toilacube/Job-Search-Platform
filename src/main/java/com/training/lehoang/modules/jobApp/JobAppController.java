package com.training.lehoang.modules.jobApp;

import com.training.lehoang.dto.request.JobApplicationRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.JobApplicationResponse;
import com.training.lehoang.entity.JobApplication;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class JobAppController {

    private final JobAppService jobAppService;

    @PostMapping(path ="",  consumes = {MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<JobApplicationResponse> jobApp(@ModelAttribute JobApplicationRequest jobApplicationRequest) {
        MultipartFile resume = jobApplicationRequest.getResume();
        String coverLetter = jobApplicationRequest.getCoverLetter();
        Integer jobId = jobApplicationRequest.getJobId();


        if (resume == null) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }

        JobApplicationResponse jobAppRes = this.jobAppService.sendApplication(jobId, resume, coverLetter );

        return ApiResponse.<JobApplicationResponse>builder()
                .message("Application sent successfully")
                .data(jobAppRes)
                .build();
    }
}
