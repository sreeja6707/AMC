package com.amc.dao;

import com.amc.model.User;
import com.amc.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    public User authenticate(String username, String password, String directorate) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ? AND directorate = ? AND status = 'Active'";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, directorate);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setDirectorate(resultSet.getString("directorate"));
                user.setFullName(resultSet.getString("full_name"));
                user.setEmail(resultSet.getString("email"));
                user.setStatus(resultSet.getString("status"));
                user.setCreatedDate(resultSet.getTimestamp("created_date"));
                return user;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE status = 'Active' ORDER BY full_name";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setDirectorate(resultSet.getString("directorate"));
                user.setFullName(resultSet.getString("full_name"));
                user.setEmail(resultSet.getString("email"));
                user.setStatus(resultSet.getString("status"));
                user.setCreatedDate(resultSet.getTimestamp("created_date"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setDirectorate(resultSet.getString("directorate"));
                user.setFullName(resultSet.getString("full_name"));
                user.setEmail(resultSet.getString("email"));
                user.setStatus(resultSet.getString("status"));
                user.setCreatedDate(resultSet.getTimestamp("created_date"));
                return user;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
