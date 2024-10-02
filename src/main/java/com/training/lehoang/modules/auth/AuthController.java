package com.training.lehoang.modules.auth;

import com.training.lehoang.dto.request.AuthRequest;
import com.training.lehoang.dto.request.OtpRequest;
import com.training.lehoang.dto.request.RegisterRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.AuthResponse;
import com.training.lehoang.modules.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AuthRequest authRequest) throws MessagingException, UnsupportedEncodingException {

        return this.authService.authenticate(authRequest);
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthResponse authResponse = this.authService.register(registerRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .message("User registered successfully")
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<AuthResponse> verifyOtp(@RequestBody OtpRequest otpRequest){
        AuthResponse authResponse = this.authService.verifyOtp(otpRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .build();
    }

    @PostMapping("/2fa")
    public ApiResponse<String> set2FA(@RequestParam int isEnable){
            this.userService.set2FA(isEnable);
            String result = isEnable > 0 ? "enabled" : "disabled";
            return ApiResponse.<String>builder().message("2FA " + result).build();
    }

}

