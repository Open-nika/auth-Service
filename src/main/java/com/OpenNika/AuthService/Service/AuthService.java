package com.OpenNika.AuthService.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.OpenNika.AuthService.Dto.AuthRequest;
import com.OpenNika.AuthService.Dto.JwtResponse;
import com.OpenNika.AuthService.Entity.RefreshToken;
import com.OpenNika.AuthService.Entity.UserEntity;
import com.OpenNika.AuthService.Repository.AuthRepository;
import com.OpenNika.AuthService.Utility.JwtUtility;

@Service
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    private AuthRepository authRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtility jwtUtility = new com.OpenNika.AuthService.Utility.JwtUtility();
    AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    public void registerUser(com.OpenNika.AuthService.Dto.AuthRequest request) {
        // Registration logic here
        UserEntity user = new UserEntity();
        user.setUserID(request.getUserID());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole());
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
          boolean roleauthenticated = user.getRole() == request.getRole();  
            String accessToken = jwtUtility.generateToken(user.getUserID(), user.getRole());
            RefreshToken refreshToken =refreshTokenService.createRefreshToken(Long.valueOf()user.getUserID()));

            if( !(matches && roleauthenticated) ) {
                throw new RuntimeException("Invalid credentials or role");
            }
         
         
            return new JwtResponse(accessToken, refreshToken.getToken());

    }



        
    }

}
