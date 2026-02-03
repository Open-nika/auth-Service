package com.OpenNika.AuthService.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OpenNika.AuthService.Dto.AuthRequest;
import com.OpenNika.AuthService.Dto.JwtResponse;
import com.OpenNika.AuthService.Service.AuthService;
import com.OpenNika.AuthService.Utility.JwtUtility;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;
    private final JwtUtility jwtUtility;
    public AuthController(AuthService authService, JwtUtility jwtUtility) {
        this.authService = authService;
        this.jwtUtility = jwtUtility;
    }

    @GetMapping("/public/login")
    public JwtResponse login(@RequestBody AuthRequest request){
        JwtResponse token = authService.login(request);
        return token;
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
    public String registerUser(@RequestBody AuthRequest request) {
        authService.registerUser(request);
        return "User registered successfully";
    }

    @GetMapping("/protected/profile")
    public ResponseEntity<String> profile(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok("Hello " + userId);
    }


}
