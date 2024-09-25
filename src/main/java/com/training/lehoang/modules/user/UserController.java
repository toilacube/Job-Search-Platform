package com.training.lehoang.modules.user;

import com.training.lehoang.dto.request.UpdateUserRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.UserResponse;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ApiResponse<UserResponse> updateUser(@PathVariable int id, @RequestBody UpdateUserRequest updateUserRequest) {
        return ApiResponse.<UserResponse>builder().data(this.userService.updateUser(id, updateUserRequest)).build();
    }

    @PostMapping(path ="/resume",  consumes = {MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<String> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }

        try {
            // Get the file's original name
            String fileName = file.getOriginalFilename();;
            // Do something with the file (e.g., save it to the server)
            byte[] bytes = file.getBytes();
            // You can write the file to the filesystem or database here
            String res = this.userService.uploadResume(file);

            return ApiResponse.<String>builder().message(res).build();
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }


    }


    @GetMapping("/test")
    public String test() {
        return this.userService.test();
    }
}

