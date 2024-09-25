package com.training.lehoang.modules.auth;

import com.training.lehoang.dto.request.AuthRequest;
import com.training.lehoang.dto.request.RegisterRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest){
        AuthResponse authResponse = this.authService.authenticate(authRequest);

        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthResponse authResponse = this.authService.register(registerRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .message("User registered successfully")
                .build();
    }

}

