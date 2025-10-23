package com.amc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/amc_tracking_system";
    private static final String DB_USERNAME = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = "password"; // Change to your MySQL password
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Load MySQL JDBC Driver
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
