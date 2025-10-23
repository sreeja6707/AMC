// Login Page JavaScript
document.addEventListener('DOMContentLoaded', function() {
    
    // Password toggle functionality
    const togglePassword = document.getElementById('togglePassword');
    const passwordField = document.getElementById('passwordField');
    const eyeIcon = document.getElementById('eyeIcon');
    
    if (togglePassword && passwordField && eyeIcon) {
        togglePassword.addEventListener('click', function() {
            // Toggle password visibility
            const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordField.setAttribute('type', type);
            
            // Toggle eye icon
            eyeIcon.classList.toggle('fa-eye');
            eyeIcon.classList.toggle('fa-eye-slash');
        });
    }
    
    // Form validation and submission
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            const username = document.querySelector('input[name="username"]').value.trim();
            const password = document.querySelector('input[name="password"]').value;
            const directorate = document.querySelector('select[name="directorate"]').value;
            
            // Basic validation
            if (!username || !password || !directorate) {
                e.preventDefault();
                showAlert('Please fill in all required fields.', 'danger');
                return false;
            }
            
            if (username.length < 3) {
                e.preventDefault();
                showAlert('Username must be at least 3 characters long.', 'danger');
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                showAlert('Password must be at least 6 characters long.', 'danger');
                return false;
            }
            
            // Show loading state
            const submitButton = loginForm.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = `
                    <span class="spinner-border spinner-border-sm me-2" role="status"></span>
                    Logging in...
                `;
            }
            
            return true;
        });
    }
    
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            if (alert) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });
    
    // Focus on username field on page load
    const usernameField = document.querySelector('input[name="username"]');
    if (usernameField) {
        usernameField.focus();
    }
});

function showAlert(message, type = 'info') {
    const alertContainer = document.querySelector('.login-body');
    if (!alertContainer) return;
    
    // Remove existing alerts
    const existingAlerts = alertContainer.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
    
    // Create new alert
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        <i class="fas fa-${getIconForType(type)} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Insert at the beginning of login body
    const form = alertContainer.querySelector('form');
    alertContainer.insertBefore(alertDiv, form);
    
    // Auto-dismiss after 5 seconds
    setTimeout(function() {
        const bsAlert = new bootstrap.Alert(alertDiv);
        bsAlert.close();
    }, 5000);
}

function getIconForType(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-circle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}
