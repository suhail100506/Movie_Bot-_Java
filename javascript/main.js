// MovieBot Website Main JavaScript
// Global variables and configuration

const API_BASE_URL = '/api';  // Replace with your Spring Boot API URL
let currentUser = JSON.parse(localStorage.getItem('moviebot_user')) || null;

// Initialize the website
document.addEventListener('DOMContentLoaded', function() {
    initializeWebsite();
    setupEventListeners();
    updateAuthDisplay();
});

// Initialize website components
function initializeWebsite() {
    // Add smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Initialize search functionality
    initializeSearch();

    // Initialize movie cards interactions
    initializeMovieCards();

    // Initialize filters
    initializeFilters();

    // Add loading animations
    addLoadingAnimations();
}

// Setup event listeners
function setupEventListeners() {
    // Search form submission
    const searchForms = document.querySelectorAll('.search-form');
    searchForms.forEach(form => {
        form.addEventListener('submit', handleSearchSubmit);
    });

    // Filter changes
    const filters = document.querySelectorAll('.filter-select');
    filters.forEach(filter => {
        filter.addEventListener('change', handleFilterChange);
    });

    // Movie card interactions
    document.addEventListener('click', handleMovieCardClick);

    // Authentication buttons
    const authButtons = document.querySelectorAll('.nav-auth a');
    authButtons.forEach(button => {
        button.addEventListener('click', handleAuthClick);
    });
}

// Initialize search functionality
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input, .filter-search');

    searchInputs.forEach(input => {
        // Real-time search
        let searchTimeout;
        input.addEventListener('input', function(e) {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                performSearch(e.target.value);
            }, 300);
        });
    });
}

// Handle search form submission
function handleSearchSubmit(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const searchQuery = formData.get('q') || e.target.querySelector('input').value;

    if (searchQuery.trim()) {
        // Redirect to movies page with search parameter
        window.location.href = `movies.html?search=${encodeURIComponent(searchQuery)}`;
    }
}

// Perform real-time search
function performSearch(query) {
    if (!query.trim()) return;

    console.log('Searching for:', query);

    // In a real application, make API call here
    // Example: fetchMovies({ search: query });

    // Mock search functionality
    const movieCards = document.querySelectorAll('.movie-card');
    movieCards.forEach(card => {
        const title = card.querySelector('h3')?.textContent.toLowerCase();
        const isMatch = title && title.includes(query.toLowerCase());

        card.style.display = isMatch ? 'block' : 'none';
    });
}

// Initialize movie cards
function initializeMovieCards() {
    const movieCards = document.querySelectorAll('.movie-card');

    movieCards.forEach(card => {
        // Add hover effects
        card.addEventListener('mouseenter', function() {
            this.classList.add('hovered');
        });

        card.addEventListener('mouseleave', function() {
            this.classList.remove('hovered');
        });

        // Add click handlers for buttons
        const detailBtn = card.querySelector('.btn-primary');
        if (detailBtn) {
            detailBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                const movieId = this.getAttribute('data-movie-id') || '1';
                window.location.href = `movie-detail.html?id=${movieId}`;
            });
        }

        const addToListBtn = card.querySelector('.add-to-list');
        if (addToListBtn) {
            addToListBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                const movieId = this.getAttribute('data-movie-id') || Math.floor(Math.random() * 1000);
                addToWatchlist(movieId);
            });
        }
    });
}

// Initialize filters
function initializeFilters() {
    // Get URL parameters for initial filter state
    const urlParams = new URLSearchParams(window.location.search);
    const genre = urlParams.get('genre');
    const search = urlParams.get('search');

    // Apply initial filters
    if (genre) {
        const genreFilter = document.getElementById('genreFilter');
        if (genreFilter) {
            genreFilter.value = genre;
            filterMoviesByGenre(genre);
        }
    }

    if (search) {
        const searchInputs = document.querySelectorAll('.search-input, .filter-search');
        searchInputs.forEach(input => {
            input.value = search;
        });
        performSearch(search);
    }
}

// Handle filter changes
function handleFilterChange(e) {
    const filterType = e.target.id;
    const filterValue = e.target.value;

    switch(filterType) {
        case 'genreFilter':
            filterMoviesByGenre(filterValue);
            break;
        case 'yearFilter':
            filterMoviesByYear(filterValue);
            break;
        case 'ratingFilter':
            filterMoviesByRating(filterValue);
            break;
    }

    // Update URL parameters
    updateURLParams(filterType.replace('Filter', ''), filterValue);
}

// Filter movies by genre
function filterMoviesByGenre(genre) {
    const movieCards = document.querySelectorAll('.movie-card');

    movieCards.forEach(card => {
        if (!genre) {
            card.style.display = 'block';
            return;
        }

        const genres = card.querySelectorAll('.genre-tag');
        const hasGenre = Array.from(genres).some(tag => 
            tag.textContent.toLowerCase().includes(genre.toLowerCase())
        );

        card.style.display = hasGenre ? 'block' : 'none';
    });
}

// Filter movies by year
function filterMoviesByYear(year) {
    const movieCards = document.querySelectorAll('.movie-card');

    movieCards.forEach(card => {
        if (!year) {
            card.style.display = 'block';
            return;
        }

        const movieYear = card.querySelector('.movie-year')?.textContent;
        const isMatch = movieYear && movieYear.includes(year);

        card.style.display = isMatch ? 'block' : 'none';
    });
}

// Filter movies by rating
function filterMoviesByRating(ratingFilter) {
    const movieCards = document.querySelectorAll('.movie-card');

    movieCards.forEach(card => {
        if (!ratingFilter) {
            card.style.display = 'block';
            return;
        }

        const ratingElement = card.querySelector('.movie-rating');
        if (!ratingElement) return;

        const rating = parseFloat(ratingElement.textContent.replace('⭐ ', ''));
        const minRating = parseFloat(ratingFilter.replace('+', ''));

        const isMatch = rating >= minRating;
        card.style.display = isMatch ? 'block' : 'none';
    });
}

// Update URL parameters
function updateURLParams(key, value) {
    const url = new URL(window.location);
    if (value) {
        url.searchParams.set(key, value);
    } else {
        url.searchParams.delete(key);
    }
    window.history.replaceState({}, '', url);
}

// Handle movie card clicks
function handleMovieCardClick(e) {
    const movieCard = e.target.closest('.movie-card');
    if (!movieCard || e.target.closest('button') || e.target.closest('a')) return;

    // Extract movie ID (in real app, this would be from data attribute)
    const movieTitle = movieCard.querySelector('h3')?.textContent;
    const movieId = Math.floor(Math.random() * 1000); // Mock ID

    window.location.href = `movie-detail.html?id=${movieId}`;
}

// Handle authentication clicks
function handleAuthClick(e) {
    // Auth pages will handle their own logic
    console.log('Auth click:', e.target.textContent);
}

// Add to watchlist
function addToWatchlist(movieId) {
    if (!currentUser) {
        showNotification('Please login to add movies to your watchlist', 'warning');
        window.location.href = 'login.html';
        return;
    }

    // Get current watchlist from localStorage
    let watchlist = JSON.parse(localStorage.getItem('moviebot_watchlist')) || [];

    // Check if movie is already in watchlist
    if (watchlist.includes(movieId)) {
        showNotification('Movie is already in your watchlist', 'info');
        return;
    }

    // Add movie to watchlist
    watchlist.push(movieId);
    localStorage.setItem('moviebot_watchlist', JSON.stringify(watchlist));

    showNotification('Movie added to your watchlist!', 'success');

    // Update UI
    updateWatchlistButton(movieId, true);
}

// Remove from watchlist
function removeFromWatchlist(movieId) {
    let watchlist = JSON.parse(localStorage.getItem('moviebot_watchlist')) || [];
    watchlist = watchlist.filter(id => id !== movieId);
    localStorage.setItem('moviebot_watchlist', JSON.stringify(watchlist));

    showNotification('Movie removed from watchlist', 'info');
    updateWatchlistButton(movieId, false);
}

// Update watchlist button state
function updateWatchlistButton(movieId, inWatchlist) {
    const buttons = document.querySelectorAll(`[data-movie-id="${movieId}"] .add-to-list`);
    buttons.forEach(button => {
        if (inWatchlist) {
            button.innerHTML = '<i class="fas fa-check"></i>';
            button.classList.add('added');
            button.onclick = () => removeFromWatchlist(movieId);
        } else {
            button.innerHTML = '<i class="fas fa-plus"></i>';
            button.classList.remove('added');
            button.onclick = () => addToWatchlist(movieId);
        }
    });
}

// Update authentication display
function updateAuthDisplay() {
    const authContainer = document.querySelector('.nav-auth');
    if (!authContainer) return;

    if (currentUser) {
        authContainer.innerHTML = `
            <span class="user-greeting">Hello, ${currentUser.name}!</span>
            <button class="btn btn-outline" onclick="logout()">Logout</button>
        `;
    } else {
        authContainer.innerHTML = `
            <a href="login.html" class="btn btn-outline">Login</a>
            <a href="register.html" class="btn btn-primary">Sign Up</a>
        `;
    }
}

// Logout function
function logout() {
    currentUser = null;
    localStorage.removeItem('moviebot_user');
    localStorage.removeItem('moviebot_watchlist');
    updateAuthDisplay();
    showNotification('You have been logged out', 'info');

    // Redirect to home page
    if (window.location.pathname !== '/index.html' && window.location.pathname !== '/') {
        window.location.href = 'index.html';
    }
}

// Show notifications
function showNotification(message, type = 'info') {
    // Remove existing notifications
    const existing = document.querySelectorAll('.notification');
    existing.forEach(notif => notif.remove());

    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;

    // Style the notification
    Object.assign(notification.style, {
        position: 'fixed',
        top: '100px',
        right: '20px',
        padding: '1rem 1.5rem',
        borderRadius: '10px',
        color: 'white',
        fontWeight: '500',
        zIndex: '9999',
        maxWidth: '300px',
        boxShadow: '0 10px 25px rgba(0,0,0,0.2)',
        transform: 'translateX(100%)',
        transition: 'transform 0.3s ease',
        fontFamily: 'Poppins, sans-serif'
    });

    // Set background based on type
    const colors = {
        success: 'linear-gradient(135deg, #10b981, #059669)',
        warning: 'linear-gradient(135deg, #f59e0b, #d97706)',
        error: 'linear-gradient(135deg, #ef4444, #dc2626)',
        info: 'linear-gradient(135deg, #3b82f6, #2563eb)'
    };
    notification.style.background = colors[type] || colors.info;

    // Add to page
    document.body.appendChild(notification);

    // Animate in
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);

    // Remove after 4 seconds
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 4000);
}

// Add loading animations
function addLoadingAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in-up');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observe elements for animation
    const animatedElements = document.querySelectorAll('.movie-card, .feature-card, .genre-card-large');
    animatedElements.forEach(el => observer.observe(el));
}

// Utility functions
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

function formatRating(rating) {
    return `⭐ ${parseFloat(rating).toFixed(1)}`;
}

function formatRuntime(minutes) {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
}

// API functions (mock implementations)
async function fetchMovies(filters = {}) {
    // In a real application, this would make an actual API call
    console.log('Fetching movies with filters:', filters);

    // Mock implementation
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                movies: [],
                totalPages: 10,
                currentPage: 1
            });
        }, 500);
    });
}

async function fetchMovieDetails(movieId) {
    console.log('Fetching movie details for ID:', movieId);

    // Mock implementation
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                id: movieId,
                title: 'Sample Movie',
                overview: 'This is a sample movie description...',
                rating: 8.5,
                year: 2024
            });
        }, 500);
    });
}

// Initialize watchlist state on page load
document.addEventListener('DOMContentLoaded', function() {
    const watchlist = JSON.parse(localStorage.getItem('moviebot_watchlist')) || [];
    watchlist.forEach(movieId => {
        updateWatchlistButton(movieId, true);
    });
});

// Handle page-specific functionality
const currentPage = window.location.pathname.split('/').pop().replace('.html', '');

switch(currentPage) {
    case 'index':
    case '':
        // Homepage specific functionality
        console.log('Homepage loaded');
        break;
    case 'movies':
        // Movies page specific functionality
        console.log('Movies page loaded');
        break;
    case 'genres':
        // Genres page specific functionality
        console.log('Genres page loaded');
        break;
    default:
        console.log(`Page loaded: ${currentPage}`);
}

// Export functions for use in other scripts
window.MovieBot = {
    showNotification,
    addToWatchlist,
    removeFromWatchlist,
    updateAuthDisplay,
    logout,
    formatDate,
    formatRating,
    formatRuntime
};