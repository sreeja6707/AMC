package com.amc.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String directorate;
    private String fullName;
    private String email;
    private String status;
    private Timestamp createdDate;
    
    public User() {}
    
    public User(String username, String password, String directorate, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.directorate = directorate;
        this.fullName = fullName;
        this.email = email;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDirectorate() { return directorate; }
    public void setDirectorate(String directorate) { this.directorate = directorate; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", directorate='" + directorate + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
