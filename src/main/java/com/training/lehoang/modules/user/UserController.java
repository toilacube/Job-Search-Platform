package com.training.lehoang.modules.user;

import com.training.lehoang.dto.request.SkillRequest;
import com.training.lehoang.dto.request.UpdateUserRequest;
import com.training.lehoang.dto.response.*;
import com.training.lehoang.entity.Feedback;
import com.training.lehoang.entity.JobApplication;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.jobApp.JobAppRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/*
*
Users must be able to create, update, and view their profile.
Profile fields should include personal details (name, contact info, education, experience, skills).
Users should be able to upload a resume.
Profile data should be stored in PostgreSQL
* */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ApiResponse<UserResponse> getUserInfo() {

        User user = this.userService.getUser();

        var userRes = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .contactInfo(user.getContactInfo())
                .build();
        return ApiResponse.<UserResponse>builder()
                .data(userRes)
                .build();
    }

    @PatchMapping("{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable int id,
            @RequestBody UpdateUserRequest updateUserRequest) {
        return ApiResponse.<UserResponse>builder().data(this.userService.updateUser(id, updateUserRequest)).build();
    }

    @PostMapping(path = "/resume", consumes = { MULTIPART_FORM_DATA_VALUE })
    public ApiResponse<ResumeResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }

        try {
            // Get the file's original name
            String fileName = file.getOriginalFilename();
            ;
            // Do something with the file (e.g., save it to the server)
            byte[] bytes = file.getBytes();
            // You can write the file to the filesystem or database here
            String res = this.userService.uploadResume(file);

            return ApiResponse.<ResumeResponse>builder().data(ResumeResponse.builder().url(res).build()).build();
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    @PostMapping("/skill")
    public ApiResponse<ArrayList<SkillResponse>> addSkills (@RequestBody SkillRequest skillRequest) {
        return ApiResponse.<ArrayList<SkillResponse>>builder().data(this.userService.addSkill(skillRequest)).build();
    }

    @GetMapping("/skill")
    public ApiResponse<ArrayList<SkillResponse>> getSkills() {
        return ApiResponse.<ArrayList<SkillResponse>>builder().data(this.userService.getSkills()).build();
    }

    @GetMapping("/job")
    public  ApiResponse<ArrayList<JobResponse>> getJobRecommendation() {
        return ApiResponse.<ArrayList<JobResponse>>builder().data(this.userService.getJobRecommendation()).build();
    }

    @GetMapping("/feedback")
    public ApiResponse<ArrayList<UserFeedbackResponse>> getApplicationFeedback() {
        User user = this.userService.getUser();
        ArrayList<UserFeedbackResponse> feedbacks = this.userService.getApplicationFeedback(user.getId());
        return ApiResponse.<ArrayList<UserFeedbackResponse>>builder().data(feedbacks).build();
    }

    @GetMapping("/test")
    public String test() {
        return this.userService.test();
    }
}
