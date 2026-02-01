package com.OpenNika.AuthService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OpenNika.AuthService.Entity.UserEntity;

@Repository

public interface AuthRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserID(String userID);

    
}
