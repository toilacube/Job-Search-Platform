package com.training.lehoang.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION( "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY( "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED( "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID( "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD( "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED( "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED( "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB( "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS( "Email already exists", HttpStatus.BAD_REQUEST),
    UPLOAD_FILE_FAILED( "Upload file failed", HttpStatus.INTERNAL_SERVER_ERROR),
    JOB_NOT_FOUND( "Job not found", HttpStatus.NOT_FOUND),
    SKILL_NOT_EXISTED( "Skill not existed", HttpStatus.BAD_REQUEST),
    SKILL_EXISTED( "Skill existed", HttpStatus.BAD_REQUEST),
    APPLICATION_NOT_FOUND( "Application not found", HttpStatus.BAD_REQUEST),
    NO_APPLICATION_FOUND( "No application found", HttpStatus.NOT_FOUND),
    ;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }


}