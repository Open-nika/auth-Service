package com.OpenNika.AuthService.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.OpenNika.AuthService.Dto.JwtResponse;
import com.OpenNika.AuthService.Entity.RefreshToken;
import com.OpenNika.AuthService.Entity.UserEntity;
import com.OpenNika.AuthService.Repository.AuthRepository;
import com.OpenNika.AuthService.Repository.RefreshTokenRepository;
import com.OpenNika.AuthService.Utility.JwtUtility;

@Service

public class RefreshTokenService {
    
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthRepository authRepository;
    private final JwtUtility jwtUtility;;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, AuthRepository authRepository, JwtUtility jwtUtility) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.authRepository = authRepository;
        this.jwtUtility = jwtUtility;
    }

    public RefreshToken createRefreshToken(Long userId) {
        
        UserEntity user = authRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        refreshTokenRepository.deleteByUserId(Long.valueOf(user.getUserID()));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    public JwtResponse refresh(String requestRefreshToken) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        
        verifyExpiration(refreshToken);

        UserEntity user = refreshToken.getUser();

        refreshTokenRepository.delete(refreshToken);

        RefreshToken newRefreshToken =
                createRefreshToken(Long.valueOf(user.getUserID()));

        String newAccessToken =
                jwtUtility.generateToken(user.getUserID(), user.getRole());

        return new JwtResponse(newAccessToken, newRefreshToken.getToken());
    }



}
