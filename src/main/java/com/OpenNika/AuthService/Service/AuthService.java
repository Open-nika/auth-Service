package com.OpenNika.AuthService.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.OpenNika.AuthService.Dto.AuthRequest;
import com.OpenNika.AuthService.Dto.JwtResponse;
import com.OpenNika.AuthService.Entity.UserEntity;
import com.OpenNika.AuthService.Repository.AuthRepository;
import com.OpenNika.AuthService.Utility.JwtUtility;

@Service
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    private AuthRepository authRepository;
    private final JwtUtility jwtUtility = new com.OpenNika.AuthService.Utility.JwtUtility();
    AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(com.OpenNika.AuthService.Dto.AuthRequest request) {
        // Registration logic here
        UserEntity user = new UserEntity();
        user.setUserID(request.getUserID());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        authRepository.save(user);

        System.out.println("Registering user: " + user.getUserID());
    }

    public void userlist() {
        System.out.println("Fetching user list...");
        authRepository.findAll().forEach(user -> {
            System.out.println("UserID: " + user.getUserID());
        });        
    }

    public JwtResponse login(AuthRequest request) {

        UserEntity user = authRepository.findByUserID(request.getUserID());

          boolean matches = passwordEncoder.matches(
            request.getPassword(),     // raw password
            user.getPassword()         // hashed password from DB
            );

        JwtResponse token = null;
        if (user != null && matches) {
            System.out.println("Login successful for user: " + request.getUserID());
             token=new JwtResponse(jwtUtility.generateToken(request.getUserID()));
        } 
        else {
             token=new JwtResponse("Invalid credentials");  
        }    
        

        return token; 
        
    }

}
