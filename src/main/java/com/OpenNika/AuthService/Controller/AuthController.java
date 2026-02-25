package com.OpenNika.AuthService.Controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OpenNika.AuthService.Dto.ApiResponse;
import com.OpenNika.AuthService.Dto.AuthRequest;
import com.OpenNika.AuthService.Dto.JwtResponse;
import com.OpenNika.AuthService.Service.AuthService;
import com.OpenNika.AuthService.Service.RefreshTokenService;
import com.OpenNika.AuthService.Utility.JwtUtility;


@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;
    private final JwtUtility jwtUtility;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, JwtUtility jwtUtility, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtUtility = jwtUtility;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/public/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody AuthRequest request){
        JwtResponse token = authService.login(request);
        ApiResponse<JwtResponse> response = new ApiResponse<>(true, "Login successful", token);
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/users")
    // public void getUsers(@RequestHeader("Authorization") String token){
    //    System.out.println("Received token: " + token);
    //     boolean authenticated=jwtUtility.validateToken(token);
    //     if (authenticated) {
    //         System.out.println("Token is valid. Fetching user list...");
    //     }
    //     else{
    //         System.out.println("Invalid token. Access denied.");
    //     }
    // }


    @PostMapping("/public/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody AuthRequest request) {
        authService.registerUser(request);
        ApiResponse<String> response = new ApiResponse<>(true, "User registered successfully", null);
        return ResponseEntity.ok(response);
    }

   @GetMapping("/user/profile")
    public ResponseEntity<ApiResponse<String>> userProfile(Authentication authentication) {
        String userId = authentication.getName();
        ApiResponse<String> response = new ApiResponse<>(true, "User profile retrieved", "Profile of " + userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> adminDashboard(Authentication authentication) {
         System.out.println("Principal: " + authentication.getName());
    System.out.println("Authorities: " + authentication.getAuthorities());

        ApiResponse<String> response = new ApiResponse<>(true, "Admin dashboard accessed", "Admin dashboard content");
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestBody Map <String,String> refreshToken){
        String token = refreshToken.get("refreshToken");
        JwtResponse newToken = refreshTokenService.refresh(token);
        System.out.println("New token generated: " + newToken.getAccessToken());
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", newToken));
    }
    





}
