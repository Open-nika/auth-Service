package com.OpenNika.AuthService.Entity;

import com.OpenNika.AuthService.Dto.Role;

import jakarta.persistence.*;


@Entity
@Table(name = "users_table")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userID;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }       

    public void setRole(Role role) {
        this.role = role;
    }
    
}