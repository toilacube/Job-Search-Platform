package com.training.lehoang.modules.auth;

import com.google.common.cache.LoadingCache;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.training.lehoang.dto.request.AuthRequest;
import com.training.lehoang.dto.request.OtpRequest;
import com.training.lehoang.dto.request.RegisterRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.dto.response.AuthResponse;
import com.training.lehoang.entity.Role;
import com.training.lehoang.entity.RoleEnum;
import com.training.lehoang.entity.User;
import com.training.lehoang.entity.UsersRole;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.mail.MailService;
import com.training.lehoang.modules.role.RoleRepo;
import com.training.lehoang.modules.role.UsersRolesRepo;
import com.training.lehoang.modules.user.CustomUserDetails;
import com.training.lehoang.modules.user.UserRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import java.security.Security;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UsersRolesRepo usersRolesRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secretKey;
    private final LoadingCache<String, Integer> otpCache ;

    // @Async
    // public CompletableFuture<UsersRole> saveUserRoleAsync(UsersRole usersRole) {
    //     UsersRole savedRole = usersRolesRepo.save(usersRole);
    //     return CompletableFuture.completedFuture(savedRole);
    // }

    public AuthResponse register(RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        if (this.userRepo.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        String password = registerRequest.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);

        Role userRole = roleRepo.findByName(RoleEnum.USER.name());

        User user = new User();
        user.setPasswordHash(encryptedPassword);
        user.setEmail(email);
        user.setContactInfo(registerRequest.getContactInfo());
        user.setName(registerRequest.getName());
        User newUser = this.userRepo.save(user);

        System.out.println(userRole.toString());

        UsersRole usersRole = new UsersRole();
        usersRole.setUser(newUser);
        usersRole.setRole(userRole);
        this.usersRolesRepo.save(usersRole);

        newUser.setUsersRoles(new HashSet<>(Arrays.asList(usersRole)));

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(newUser.getEmail(), password);

        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        String token = this.createToken(authenticationResponse);
        return AuthResponse.builder().accessToken(token).build();

    }

    public ApiResponse<?> authenticate(AuthRequest authRequest) throws MessagingException, UnsupportedEncodingException {
        User user = this.userRepo.findByEmail(authRequest.getEmail()).orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches( authRequest.getPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // if user doesnt enable 2FA, return token
        if (!user.getIs2FA()){
            String token = this.createToken(new CustomUserDetails(user));
            return ApiResponse.<AuthResponse>builder().data(
                    AuthResponse.builder().accessToken(token).build()
            ).build();
        }

        this.sendOtp(authRequest.getEmail());

        return ApiResponse.builder().data(getOtpSendMessage()).build();
    }

    private Map<String, String> getOtpSendMessage() {
        final var response = new HashMap<String, String>();
        response.put("message",
                "OTP sent successfully sent to your registered email-address. verify it using /verify-otp endpoint");
        return response;
    }

    public void sendOtp(String email) throws MessagingException, UnsupportedEncodingException {
        try {
            otpCache.get(email);
            otpCache.invalidate(email);
        } catch (ExecutionException e) {
            throw new AppException(ErrorCode.UNKNOWN_ERROR);
        }
        final int otp = new Random().ints(1, 100000, 999999).sum();
        System.out.println(otp);
        otpCache.put(email, otp);

        CompletableFuture.supplyAsync(() -> {
            try {
                this.mailService.sendOtpWithMail(email, otp);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return HttpStatus.OK;
        });
    }

    public AuthResponse verifyOtp(OtpRequest otpRequest) {
        User user = this.userRepo.findByEmail(otpRequest.getEmail()).orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_EXISTED));
        Integer otpCode;
        try {
            otpCode = this.otpCache.get(otpRequest.getEmail());
        } catch (ExecutionException e) {
            throw new AppException(ErrorCode.UNKNOWN_ERROR);
        }

        if(otpCode.equals(otpRequest.getOtp())) {
            String token = this.createToken(new CustomUserDetails(user));
            return AuthResponse.builder().accessToken(token).build();
        }
        else throw new AppException(ErrorCode.INVALID_OTP);
    }


    public String createToken(Authentication authentication) {

        CustomUserDetails userDetails = authentication.getPrincipal() instanceof CustomUserDetails ? (CustomUserDetails) authentication.getPrincipal() : null;
        System.out.println(userDetails.getAuthorities());

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

    public String createToken(CustomUserDetails userDetails) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
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
