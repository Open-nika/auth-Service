package com.OpenNika.AuthService.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OpenNika.AuthService.Dto.AuthRequest;
import com.OpenNika.AuthService.Service.AuthService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users")
    public String userlist(){
        authService.userlist();
        return "User list fetched successfully";
        
    }

    @GetMapping("/login")
    public String login(@RequestBody AuthRequest request){
        authService.login(request);
        return "Login successful";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody AuthRequest request) {
        authService.registerUser(request);
        return "User registered successfully";
    }

}
