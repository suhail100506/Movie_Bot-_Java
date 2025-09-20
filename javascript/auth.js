// MovieBot Authentication JavaScript

// Initialize auth page
document.addEventListener('DOMContentLoaded', function() {
    initializeAuth();
    setupFormValidation();
    setupPasswordToggle();
    setupSocialLogin();
});

// Initialize authentication
function initializeAuth() {
    const currentPage = window.location.pathname.split('/').pop();

    // Check if user is already logged in
    const currentUser = JSON.parse(localStorage.getItem('moviebot_user'));
    if (currentUser && (currentPage === 'login.html' || currentPage === 'register.html')) {
        // Redirect to dashboard or home
        window.location.href = 'index.html';
        return;
    }

    // Setup form submissions
    const authForms = document.querySelectorAll('.auth-form');
    authForms.forEach(form => {
        form.addEventListener('submit', handleAuthSubmit);
    });

    // Setup password strength checker for register page
    if (currentPage === 'register.html') {
        setupPasswordStrength();
        setupGenreSelection();
    }
}

// Handle authentication form submission
function handleAuthSubmit(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    const formType = e.target.action.includes('login') ? 'login' : 
                     e.target.action.includes('register') ? 'register' : 
                     getFormType();

    if (formType === 'login') {
        handleLogin(formData);
    } else if (formType === 'register') {
        handleRegister(formData);
    }
}

// Determine form type based on current page
function getFormType() {
    const currentPage = window.location.pathname.split('/').pop();
    return currentPage === 'login.html' ? 'login' : 'register';
}

// Handle login
function handleLogin(formData) {
    const email = formData.get('email');
    const password = formData.get('password');
    const remember = formData.get('remember');

    // Show loading state
    const submitBtn = document.querySelector('.auth-form button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Signing in...';
    submitBtn.disabled = true;

    // Simulate API call
    setTimeout(() => {
        // Mock validation
        if (email && password) {
            // Create user object
            const user = {
                id: Date.now(),
                email: email,
                name: email.split('@')[0], // Use part before @ as name
                loginTime: new Date().toISOString(),
                preferences: {}
            };

            // Store user data
            localStorage.setItem('moviebot_user', JSON.stringify(user));

            if (remember) {
                localStorage.setItem('moviebot_remember', 'true');
            }

            // Show success message
            showAuthNotification('Login successful! Welcome back.', 'success');

            // Redirect after short delay
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 1500);
        } else {
            showAuthNotification('Please enter valid credentials', 'error');
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
        }
    }, 1500);
}

// Handle registration
function handleRegister(formData) {
    const firstName = formData.get('firstName');
    const lastName = formData.get('lastName');
    const email = formData.get('email');
    const password = formData.get('password');
    const confirmPassword = formData.get('confirmPassword');
    const genres = formData.getAll('genres');
    const terms = formData.get('terms');

    // Validation
    if (!terms) {
        showAuthNotification('Please accept the terms and conditions', 'error');
        return;
    }

    if (password !== confirmPassword) {
        showAuthNotification('Passwords do not match', 'error');
        return;
    }

    if (password.length < 6) {
        showAuthNotification('Password must be at least 6 characters long', 'error');
        return;
    }

    // Show loading state
    const submitBtn = document.querySelector('.auth-form button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Creating Account...';
    submitBtn.disabled = true;

    // Simulate API call
    setTimeout(() => {
        // Create user object
        const user = {
            id: Date.now(),
            email: email,
            name: `${firstName} ${lastName}`,
            firstName: firstName,
            lastName: lastName,
            joinDate: new Date().toISOString(),
            preferences: {
                favoriteGenres: genres
            }
        };

        // Store user data
        localStorage.setItem('moviebot_user', JSON.stringify(user));

        // Show success message
        showAuthNotification('Account created successfully! Welcome to MovieBot.', 'success');

        // Redirect after short delay
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1500);
    }, 1500);
}

// Setup form validation
function setupFormValidation() {
    const inputs = document.querySelectorAll('.auth-form input[required]');

    inputs.forEach(input => {
        input.addEventListener('blur', validateInput);
        input.addEventListener('input', clearInputError);
    });

    // Email validation
    const emailInputs = document.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('blur', validateEmail);
    });

    // Password confirmation
    const confirmPasswordInput = document.getElementById('confirmPassword');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    }
}

// Validate individual input
function validateInput(e) {
    const input = e.target;
    const value = input.value.trim();

    if (!value) {
        showInputError(input, 'This field is required');
        return false;
    }

    // Specific validations
    switch(input.type) {
        case 'email':
            return validateEmail(e);
        case 'password':
            return validatePassword(input);
        default:
            clearInputError(input);
            return true;
    }
}

// Validate email
function validateEmail(e) {
    const input = e.target;
    const email = input.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
        showInputError(input, 'Please enter a valid email address');
        return false;
    }

    clearInputError(input);
    return true;
}

// Validate password
function validatePassword(input) {
    const password = input.value;

    if (password.length < 6) {
        showInputError(input, 'Password must be at least 6 characters long');
        return false;
    }

    clearInputError(input);
    return true;
}

// Validate password match
function validatePasswordMatch() {
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('confirmPassword');

    if (!passwordInput || !confirmInput) return;

    if (passwordInput.value !== confirmInput.value) {
        showInputError(confirmInput, 'Passwords do not match');
        return false;
    }

    clearInputError(confirmInput);
    return true;
}

// Show input error
function showInputError(input, message) {
    clearInputError(input);

    input.classList.add('error');

    const errorElement = document.createElement('div');
    errorElement.className = 'input-error';
    errorElement.textContent = message;
    errorElement.style.color = '#ff6b35';
    errorElement.style.fontSize = '0.8rem';
    errorElement.style.marginTop = '0.25rem';

    input.parentNode.appendChild(errorElement);
}

// Clear input error
function clearInputError(input) {
    if (typeof input === 'object' && input.target) {
        input = input.target;
    }

    input.classList.remove('error');

    const existingError = input.parentNode.querySelector('.input-error');
    if (existingError) {
        existingError.remove();
    }
}

// Setup password toggle
function setupPasswordToggle() {
    const toggleButtons = document.querySelectorAll('.password-toggle');

    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const passwordInput = this.parentNode.querySelector('input[type="password"], input[type="text"]');
            const icon = this.querySelector('i');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.className = 'fas fa-eye-slash';
            } else {
                passwordInput.type = 'password';
                icon.className = 'fas fa-eye';
            }
        });
    });
}

// Global function for password toggle (called from HTML)
function togglePassword() {
    const passwordInput = event.target.closest('.input-group').querySelector('input');
    const icon = event.target;

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.className = 'fas fa-eye-slash';
    } else {
        passwordInput.type = 'password';
        icon.className = 'fas fa-eye';
    }
}

// Setup password strength indicator
function setupPasswordStrength() {
    const passwordInput = document.getElementById('password');
    if (!passwordInput) return;

    passwordInput.addEventListener('input', function() {
        const password = this.value;
        const strength = calculatePasswordStrength(password);
        updatePasswordStrengthUI(strength);
    });
}

// Calculate password strength
function calculatePasswordStrength(password) {
    let score = 0;
    let feedback = [];

    // Length check
    if (password.length >= 6) score += 25;
    else feedback.push('At least 6 characters');

    // Uppercase check
    if (/[A-Z]/.test(password)) score += 25;
    else feedback.push('One uppercase letter');

    // Lowercase check
    if (/[a-z]/.test(password)) score += 25;
    else feedback.push('One lowercase letter');

    // Number or special character check
    if (/[0-9!@#$%^&*]/.test(password)) score += 25;
    else feedback.push('One number or special character');

    return {
        score: score,
        feedback: feedback,
        level: score < 25 ? 'weak' : score < 50 ? 'fair' : score < 75 ? 'good' : 'strong'
    };
}

// Update password strength UI
function updatePasswordStrengthUI(strength) {
    const strengthBar = document.querySelector('.strength-fill');
    const strengthText = document.querySelector('.strength-text');

    if (!strengthBar || !strengthText) return;

    // Update bar
    strengthBar.style.width = `${strength.score}%`;

    // Update colors
    const colors = {
        weak: '#ef4444',
        fair: '#f59e0b',
        good: '#3b82f6',
        strong: '#10b981'
    };

    strengthBar.style.background = colors[strength.level] || colors.weak;

    // Update text
    const labels = {
        weak: 'Weak password',
        fair: 'Fair password',
        good: 'Good password',
        strong: 'Strong password'
    };

    strengthText.textContent = labels[strength.level] || 'Password strength';
    strengthText.style.color = colors[strength.level] || colors.weak;
}

// Setup genre selection
function setupGenreSelection() {
    const genreCheckboxes = document.querySelectorAll('.genre-checkbox input');

    genreCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const tag = this.nextElementSibling;
            if (this.checked) {
                tag.style.background = '#ff6b35';
                tag.style.color = 'white';
                tag.style.borderColor = '#ff6b35';
            } else {
                tag.style.background = 'rgba(255, 107, 53, 0.2)';
                tag.style.color = '#ff6b35';
                tag.style.borderColor = 'rgba(255, 107, 53, 0.3)';
            }
        });
    });
}

// Setup social login
function setupSocialLogin() {
    const socialButtons = document.querySelectorAll('.btn-social');

    socialButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const provider = this.textContent.includes('Google') ? 'google' : 'facebook';
            handleSocialLogin(provider);
        });
    });
}

// Handle social login
function handleSocialLogin(provider) {
    showAuthNotification(`${provider.charAt(0).toUpperCase() + provider.slice(1)} login is not implemented yet`, 'info');

    // In a real application, this would redirect to OAuth provider
    console.log(`Initiating ${provider} login...`);
}

// Show auth-specific notifications
function showAuthNotification(message, type = 'info') {
    // Remove existing notifications
    const existing = document.querySelectorAll('.auth-notification');
    existing.forEach(notif => notif.remove());

    // Create notification
    const notification = document.createElement('div');
    notification.className = `auth-notification notification-${type}`;
    notification.textContent = message;

    // Style the notification
    Object.assign(notification.style, {
        position: 'fixed',
        top: '100px',
        left: '50%',
        transform: 'translateX(-50%)',
        padding: '1rem 2rem',
        borderRadius: '10px',
        color: 'white',
        fontWeight: '500',
        zIndex: '9999',
        maxWidth: '400px',
        textAlign: 'center',
        boxShadow: '0 10px 25px rgba(0,0,0,0.2)',
        fontFamily: 'Poppins, sans-serif',
        opacity: '0',
        transition: 'opacity 0.3s ease'
    });

    // Set background based on type
    const colors = {
        success: 'linear-gradient(135deg, #10b981, #059669)',
        error: 'linear-gradient(135deg, #ef4444, #dc2626)',
        info: 'linear-gradient(135deg, #3b82f6, #2563eb)'
    };
    notification.style.background = colors[type] || colors.info;

    // Add to page
    document.body.appendChild(notification);

    // Animate in
    setTimeout(() => {
        notification.style.opacity = '1';
    }, 100);

    // Remove after 4 seconds
    setTimeout(() => {
        notification.style.opacity = '0';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 4000);
}

// Export auth functions
window.AuthSystem = {
    handleLogin,
    handleRegister,
    togglePassword,
    showAuthNotification
};