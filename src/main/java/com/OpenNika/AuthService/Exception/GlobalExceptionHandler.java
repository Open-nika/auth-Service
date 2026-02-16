package com.OpenNika.AuthService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.OpenNika.AuthService.Dto.ApiResponse;
import com.OpenNika.AuthService.Dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler  {
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex) {
        ApiResponse<?> errorResponse = new ApiResponse(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        ApiResponse<?> errorResponse = new ApiResponse(false, "Internal server error", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
