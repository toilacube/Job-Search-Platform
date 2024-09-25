package com.training.lehoang.modules.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.training.lehoang.dto.request.AuthRequest;
import com.training.lehoang.dto.request.RegisterRequest;
import com.training.lehoang.dto.response.AuthResponse;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.user.CustomUserDetails;
import com.training.lehoang.modules.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import java.security.Security;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;

    @Value("${jwt.secret}")
    private String secretKey;

    public AuthResponse register(RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        if (this.userRepo.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String password = registerRequest.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setPasswordHash(encryptedPassword);
        user.setEmail(email);
        user.setContactInfo(registerRequest.getContactInfo());
        user.setName(registerRequest.getName());

        User newUser = this.userRepo.save(user);

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(newUser.getEmail(), password);

        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        String token = this.createToken(authenticationResponse);
        return AuthResponse.builder().accessToken(token).build();

    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(authRequest.getEmail(), authRequest.getPassword());

        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        String token = this.createToken(authenticationResponse);
        return AuthResponse.builder().accessToken(token).build();
    }


    public String createToken(Authentication authentication) {

        CustomUserDetails userDetails = authentication.getPrincipal() instanceof CustomUserDetails ? (CustomUserDetails) authentication.getPrincipal() : null;
//        System.out.println(userDetails.getAuthorities());

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(authentication.getName())
                .issueTime(new Date())
                .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .expirationTime(new Date(Instant.now().plus(9999999, ChronoUnit.MINUTES).toEpochMilli()))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            JWSSigner signer = new MACSigner(secretKey);
            jwsObject.sign(signer);
            return jwsObject.serialize();
        }
         catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
