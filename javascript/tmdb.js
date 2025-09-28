// TMDB integration for Movies page via backend proxy
// Uses /api/tmdb/trending and /api/tmdb/search
(function(){
  const API_BASE = '/api/tmdb';
  const IMG_BASE = 'https://image.tmdb.org/t/p';

  function posterUrl(path, size='w342'){
    return path ? `${IMG_BASE}/${size}${path}` : 'https://via.placeholder.com/300x450/1a1a2e/ff6b35?text=No+Image';
  }

  async function fetchJson(url){
    const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
    if(!res.ok) throw new Error(`HTTP ${res.status}`);
    return res.json();
  }

  function renderMoviesGrid(results){
    const grid = document.querySelector('.movies-grid');
    if(!grid) return;
    if(!Array.isArray(results) || results.length === 0){
      grid.innerHTML = '<p style="opacity:.8">No movies found.</p>';
      return;
    }
    grid.innerHTML = results.map(m => `
      <div class="movie-card">
        <div class="movie-poster">
          <img src="${posterUrl(m.poster_path)}" alt="${(m.title||m.name||'Movie').replace(/"/g,'&quot;')}">
          <div class="movie-overlay">
            <a href="movie-detail.html?id=${m.id}" class="btn btn-primary">View Details</a>
            <button class="btn btn-outline add-to-list" data-movie-id="${m.id}"><i class="fas fa-plus"></i></button>
          </div>
        </div>
        <div class="movie-info">
          <h3>${m.title || m.name || 'Untitled'}</h3>
          <p class="movie-year">${(m.release_date||m.first_air_date||'').slice(0,4) || '—'}</p>
          <div class="movie-rating">⭐ ${(m.vote_average||0).toFixed(1)}</div>
        </div>
      </div>
    `).join('');
  }

  async function load(){
    const grid = document.querySelector('.movies-grid');
    if(!grid) return; // only run on Movies page

    const params = new URLSearchParams(window.location.search);
    const q = params.get('search') || '';
    try{
      const url = q.trim() ? `${API_BASE}/search?q=${encodeURIComponent(q)}`
                           : `${API_BASE}/trending`;
      const data = await fetchJson(url);
      renderMoviesGrid(data.results || []);
    }catch(err){
      console.error('TMDB load failed', err);
      renderMoviesGrid([]);
    }
  }

  document.addEventListener('DOMContentLoaded', load);
})();
