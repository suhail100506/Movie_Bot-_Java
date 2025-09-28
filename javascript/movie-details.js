// MovieBot Movie Detail Page JavaScript

// Get movie ID from URL
const urlParams = new URLSearchParams(window.location.search);
const movieId = urlParams.get('id') || '1';
const API_BASE = '/api/tmdb';
const IMG_BASE = 'https://image.tmdb.org/t/p';

// Initialize movie detail page
document.addEventListener('DOMContentLoaded', function() {
    initializeMovieDetail();
    setupMovieActions();
    setupRatingSystem();
    loadMovieData();
});

// Initialize movie detail functionality
function initializeMovieDetail() {
    console.log('Loading movie details for ID:', movieId);
}

// Load movie data
async function loadMovieData() {
    try {
        const res = await fetch(`${API_BASE}/movie/${movieId}`, { headers: { 'Accept': 'application/json' } });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const m = await res.json();

        // Map TMDB response to our UI fields
        const movie = {
            title: m.title || m.name || 'Untitled',
            year: (m.release_date || m.first_air_date || '').slice(0,4) || '—',
            rating: m.vote_average || 0,
            runtime: m.runtime || 0,
            director: (m.credits?.crew || []).find(p => p.job === 'Director')?.name || '—',
            cast: (m.credits?.cast || []).slice(0,6).map(p => p.name),
            genres: (m.genres || []).map(g => g.name),
            overview: m.overview || '',
            tagline: m.tagline || '',
            budget: m.budget ? `$${m.budget.toLocaleString()}` : '—',
            boxOffice: m.revenue ? `$${m.revenue.toLocaleString()}` : '—',
            language: m.original_language || '—',
            country: (m.production_countries || []).map(c => c.name).join(', ') || '—',
            keywords: (m.keywords?.keywords || m.keywords?.results || []).map(k => k.name).slice(0,10),
            streaming: [],
            poster: m.poster_path ? `${IMG_BASE}/w500${m.poster_path}` : '',
            backdrop: m.backdrop_path ? `${IMG_BASE}/original${m.backdrop_path}` : ''
        };

        updateMovieContent(movie);
        document.title = `${movie.title} (${movie.year}) - MovieBot`;

        // Update images
        const posterImg = document.querySelector('.movie-poster-large img');
        if (posterImg && movie.poster) posterImg.src = movie.poster;
        const backdropImg = document.querySelector('.movie-backdrop img');
        if (backdropImg && movie.backdrop) backdropImg.src = movie.backdrop;
    } catch (e) {
        console.error('Failed to load movie', e);
    const titleElement = document.querySelector('.movie-title-large');
    if (titleElement) {
        titleElement.textContent = movie.title;
    }

    // Update meta information
    updateMovieMeta(movie);

    // Update genres
    updateGenres(movie.genres);

    // Update tagline
    const taglineElement = document.querySelector('.movie-tagline');
    if (taglineElement) {
        taglineElement.textContent = `"${movie.tagline}"`;
    }

    // Update description
    const descriptionElement = document.querySelector('.movie-description');
    if (descriptionElement) {
        descriptionElement.textContent = movie.overview;
    }

    // Update movie info
    updateMovieInfo(movie);

    // Update keywords
    updateKeywords(movie.keywords);

    // Update streaming options
    updateStreamingOptions(movie.streaming);
}

// Update movie meta information
function updateMovieMeta(movie) {
    const metaElements = document.querySelectorAll('.movie-meta span');
    if (metaElements.length >= 4) {
        metaElements[0].textContent = movie.year;
        metaElements[1].textContent = `${movie.runtime} min`;
        metaElements[2].textContent = `⭐ ${movie.rating} (2.8M votes)`;
        metaElements[3].textContent = 'PG-13';
    }
}

// Update genres
function updateGenres(genres) {
    const genresContainer = document.querySelector('.movie-genres-large');
    if (!genresContainer) return;

    genresContainer.innerHTML = genres.map(genre => 
        `<span class="genre-tag">${genre}</span>`
    ).join('');
}

// Update movie info sidebar
function updateMovieInfo(movie) {
    const movieStats = document.querySelector('.movie-stats');
    if (!movieStats) return;

    const stats = [
        { label: 'Director:', value: movie.director },
        { label: 'Writer:', value: 'Jonathan Nolan, Christopher Nolan' },
        { label: 'Release Date:', value: `July 18, ${movie.year}` },
        { label: 'Budget:', value: movie.budget },
        { label: 'Box Office:', value: movie.boxOffice },
        { label: 'Language:', value: movie.language },
        { label: 'Country:', value: movie.country }
    ];

    movieStats.innerHTML = stats.map(stat => `
        <div class="stat-row">
            <span class="stat-label">${stat.label}</span>
            <span class="stat-value">${stat.value}</span>
        </div>
    `).join('');
}

// Update keywords
function updateKeywords(keywords) {
    const keywordsContainer = document.querySelector('.keywords');
    if (!keywordsContainer) return;

    keywordsContainer.innerHTML = keywords.map(keyword => 
        `<span class="keyword-tag">${keyword}</span>`
    ).join('');
}

// Update streaming options
function updateStreamingOptions(streamingOptions) {
    const streamingContainer = document.querySelector('.streaming-platforms');
    if (!streamingContainer) return;

    streamingContainer.innerHTML = streamingOptions.map(option => {
        const buttonText = option.type === 'subscription' ? 'Watch Now' : 
                          option.type === 'rent' ? `Rent ${option.price}` : 'View';
        const buttonClass = option.type === 'subscription' ? 'btn-primary' : 'btn-outline';

        return `
            <div class="platform-item">
                <img src="https://via.placeholder.com/60x60/${getPlatformColor(option.platform)}/fff?text=${option.platform.charAt(0)}" 
                     alt="${option.platform}" class="platform-logo">
                <div class="platform-info">
                    <h4>${option.platform}</h4>
                    <p>${option.type === 'subscription' ? 'Included with subscription' : `Rent from ${option.price}`}</p>
                    <a href="${option.url}" class="btn btn-small ${buttonClass}">${buttonText}</a>
                </div>
            </div>
        `;
    }).join('');
}

// Get platform color for placeholder
function getPlatformColor(platform) {
    const colors = {
        'Netflix': 'e50914',
        'HBO Max': '00a8e1',
        'Amazon Prime': 'ff9500',
        'Disney+': '113ccf',
        'Hulu': '1ce783'
    };
    return colors[platform] || 'ff6b35';
}

// Update breadcrumb
function updateBreadcrumb() {
    const breadcrumb = document.querySelector('.breadcrumb');
    if (breadcrumb) {
        breadcrumb.innerHTML = `
            <a href="index.html">Home</a> <span>/</span> 
            <a href="movies.html">Movies</a> <span>/</span> 
            <span>Loading...</span>
        `;
    }
}

// Check watchlist status
function checkWatchlistStatus() {
    const watchlist = JSON.parse(localStorage.getItem('moviebot_watchlist')) || [];
    const isInWatchlist = watchlist.includes(movieId);

    const watchlistBtn = document.querySelector('.btn-outline');
    if (watchlistBtn && watchlistBtn.textContent.includes('Watchlist')) {
        if (isInWatchlist) {
            watchlistBtn.innerHTML = '<i class="fas fa-check"></i> In Watchlist';
            watchlistBtn.classList.add('added');
        }
    }
}

// Toggle watchlist
function toggleWatchlist(movieId) {
    const currentUser = JSON.parse(localStorage.getItem('moviebot_user'));

    if (!currentUser) {
        if (window.MovieBot) {
            window.MovieBot.showNotification('Please login to add movies to your watchlist', 'warning');
        }
        window.location.href = 'login.html';
        return;
    }

    let watchlist = JSON.parse(localStorage.getItem('moviebot_watchlist')) || [];
    const isInWatchlist = watchlist.includes(movieId);

    const watchlistBtn = document.querySelector('.btn-outline');

    if (isInWatchlist) {
        // Remove from watchlist
        watchlist = watchlist.filter(id => id !== movieId);
        localStorage.setItem('moviebot_watchlist', JSON.stringify(watchlist));

        if (watchlistBtn) {
            watchlistBtn.innerHTML = '<i class="fas fa-plus"></i> Add to Watchlist';
            watchlistBtn.classList.remove('added');
        }

        if (window.MovieBot) {
            window.MovieBot.showNotification('Movie removed from watchlist', 'info');
        }
    } else {
        // Add to watchlist
        watchlist.push(movieId);
        localStorage.setItem('moviebot_watchlist', JSON.stringify(watchlist));

        if (watchlistBtn) {
            watchlistBtn.innerHTML = '<i class="fas fa-check"></i> In Watchlist';
            watchlistBtn.classList.add('added');
        }

        if (window.MovieBot) {
            window.MovieBot.showNotification('Movie added to your watchlist!', 'success');
        }
    }
}

// Show trailer
function showTrailer(movieId) {
    // In a real application, this would open a video modal
    if (window.MovieBot) {
        window.MovieBot.showNotification('Trailer functionality not implemented yet', 'info');
    }

    console.log('Playing trailer for movie:', movieId);

    // Mock trailer implementation
    const trailerModal = document.createElement('div');
    trailerModal.className = 'trailer-modal';
    trailerModal.innerHTML = `
        <div class="trailer-modal-content">
            <span class="trailer-close">&times;</span>
            <div class="trailer-placeholder">
                <i class="fas fa-play-circle"></i>
                <p>Trailer would play here</p>
                <p>Movie ID: ${movieId}</p>
            </div>
        </div>
    `;

    // Add styles
    trailerModal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10000;
    `;

    trailerModal.querySelector('.trailer-modal-content').style.cssText = `
        background: rgba(26, 26, 46, 0.95);
        padding: 2rem;
        border-radius: 15px;
        text-align: center;
        max-width: 500px;
        position: relative;
    `;

    trailerModal.querySelector('.trailer-close').style.cssText = `
        position: absolute;
        top: 10px;
        right: 15px;
        font-size: 2rem;
        color: #ff6b35;
        cursor: pointer;
    `;

    trailerModal.querySelector('.trailer-placeholder').style.cssText = `
        color: white;
        padding: 2rem;
    `;

    trailerModal.querySelector('.trailer-placeholder i').style.cssText = `
        font-size: 4rem;
        color: #ff6b35;
        margin-bottom: 1rem;
    `;

    document.body.appendChild(trailerModal);

    // Close modal
    trailerModal.querySelector('.trailer-close').addEventListener('click', () => {
        document.body.removeChild(trailerModal);
    });

    trailerModal.addEventListener('click', (e) => {
        if (e.target === trailerModal) {
            document.body.removeChild(trailerModal);
        }
    });
}

// Setup trailer modal
function setupTrailerModal() {
    // This would be used for actual trailer implementation
}

// Setup similar movies
function setupSimilarMovies() {
    const similarMovieCards = document.querySelectorAll('.movie-card-small');

    similarMovieCards.forEach(card => {
        card.addEventListener('click', function() {
            const title = this.querySelector('h4')?.textContent;
            const newMovieId = Math.floor(Math.random() * 100) + 1; // Mock ID

            // Navigate to the similar movie
            window.location.href = `movie-detail.html?id=${newMovieId}`;
        });

        // Add hover effects
        card.style.cursor = 'pointer';
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.05)';
            this.style.transition = 'transform 0.3s ease';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
        });
    });
}

// Open streaming platform
function openStreamingPlatform(platform, movieId) {
    if (window.MovieBot) {
        window.MovieBot.showNotification(`Opening ${platform}...`, 'info');
    }

    console.log(`Opening ${platform} for movie ${movieId}`);

    // In a real application, this would redirect to the actual streaming platform
    // For demo purposes, just show a notification
    setTimeout(() => {
        if (window.MovieBot) {
            window.MovieBot.showNotification('This would redirect to the streaming platform', 'info');
        }
    }, 1000);
}

// Export movie detail functions
window.MovieDetail = {
    toggleWatchlist,
    rateMovie,
    showTrailer,
    openStreamingPlatform
};