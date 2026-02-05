package com.OpenNika.AuthService.Dto;

public class AuthRequest {
    
    private String userID;
    private String password;
    private Role role;
    public AuthRequest() {
    }

    public AuthRequest(String userID, String password, Role role) {
        this.userID = userID;
        this.password = password;
        this.role = role;
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
