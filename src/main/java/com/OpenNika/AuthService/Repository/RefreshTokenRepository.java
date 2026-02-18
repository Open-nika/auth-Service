package com.OpenNika.AuthService.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.OpenNika.AuthService.Entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    
}
