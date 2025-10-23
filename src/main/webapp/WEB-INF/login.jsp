<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - AMC/Warranty/Status Tracking</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/login.css" rel="stylesheet">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <i class="fas fa-shield-alt login-icon"></i>
                <h2>AMC/Warranty/Status Tracking</h2>
                <p class="text-muted">Please login to continue</p>
            </div>
            
            <div class="login-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <form action="LoginServlet" method="post" id="loginForm">
                    <div class="form-group mb-3">
                        <label class="form-label">
                            <i class="fas fa-user me-2"></i>Username
                        </label>
                        <input type="text" name="username" class="form-control" 
                               placeholder="Enter your username" required>
                    </div>
                    
                    <div class="form-group mb-3">
                        <label class="form-label">
                            <i class="fas fa-lock me-2"></i>Password
                        </label>
                        <div class="input-group">
                            <input type="password" name="password" class="form-control" 
                                   placeholder="Enter your password" required id="passwordField">
                            <button type="button" class="btn btn-outline-secondary" id="togglePassword">
                                <i class="fas fa-eye" id="eyeIcon"></i>
                            </button>
                        </div>
                    </div>
                    
                    <div class="form-group mb-4">
                        <label class="form-label">
                            <i class="fas fa-building me-2"></i>Directorate
                        </label>
                        <select name="directorate" class="form-select" required>
                            <option value="">Select Directorate</option>
                            <option value="Information Technology">Information Technology</option>
                            <option value="Finance">Finance</option>
                            <option value="Human Resources">Human Resources</option>
                            <option value="Operations">Operations</option>
                        </select>
                    </div>
                    
                    <button type="submit" class="btn btn-primary btn-login">
                        <i class="fas fa-sign-in-alt me-2"></i>Login
                    </button>
                </form>
            </div>
            
            <div class="login-footer">
                <small class="text-muted">
                    <i class="fas fa-info-circle me-1"></i>
                    Contact IT support for login issues
                </small>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="js/login.js"></script>
</body>
</html>
