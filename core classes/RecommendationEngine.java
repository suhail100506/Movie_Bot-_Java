import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Recommendation Engine for movie recommendations
 */
public class RecommendationEngine {
    private MovieDatabase movieDatabase;
    private UserDatabase userDatabase;

    public RecommendationEngine(MovieDatabase movieDatabase, UserDatabase userDatabase) {
        this.movieDatabase = movieDatabase;
        this.userDatabase = userDatabase;
    }

    /**
     * Get personalized recommendations for a user
     */
    public List<Movie> getRecommendations(User user, int maxRecommendations) {
        List<Movie> recommendations = new ArrayList<>();

        // Strategy 1: Recommend based on favorite genres
        recommendations.addAll(getGenreBasedRecommendations(user, maxRecommendations / 2));

        // Strategy 2: Recommend based on similar users
        recommendations.addAll(getCollaborativeRecommendations(user, maxRecommendations / 2));

        // Strategy 3: Add popular movies if needed
        if (recommendations.size() < maxRecommendations) {
            recommendations.addAll(getPopularMovieRecommendations(user, 
                maxRecommendations - recommendations.size()));
        }

        // Remove duplicates and movies already watched
        return recommendations.stream()
                .distinct()
                .filter(movie -> !user.hasWatched(movie.getId()))
                .limit(maxRecommendations)
                .collect(Collectors.toList());
    }

    /**
     * Genre-based recommendations
     */
    private List<Movie> getGenreBasedRecommendations(User user, int count) {
        List<Movie> recommendations = new ArrayList<>();

        // Get user's favorite genres
        List<String> favoriteGenres = user.getFavoriteGenres();

        if (favoriteGenres.isEmpty()) {
            // If no favorite genres, use genres from highly rated movies
            favoriteGenres = getGenresFromUserRatings(user);
        }

        // Get movies from favorite genres
        for (String genre : favoriteGenres) {
            List<Movie> genreMovies = movieDatabase.searchByGenre(genre);

            // Sort by rating and add top movies
            genreMovies.sort((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()));

            for (Movie movie : genreMovies) {
                if (!user.hasWatched(movie.getId()) && !recommendations.contains(movie)) {
                    recommendations.add(movie);
                    if (recommendations.size() >= count) break;
                }
            }
            if (recommendations.size() >= count) break;
        }

        return recommendations;
    }

    /**
     * Collaborative filtering recommendations
     */
    private List<Movie> getCollaborativeRecommendations(User user, int count) {
        List<Movie> recommendations = new ArrayList<>();

        // Find similar users
        List<User> similarUsers = findSimilarUsers(user, 5);

        // Get highly rated movies from similar users
        Map<Integer, Double> movieScores = new HashMap<>();
        Map<Integer, Integer> movieCounts = new HashMap<>();

        for (User similarUser : similarUsers) {
            for (Map.Entry<Integer, Double> entry : similarUser.getMovieRatings().entrySet()) {
                int movieId = entry.getKey();
                double rating = entry.getValue();

                // Only consider highly rated movies (>= 4.0)
                if (rating >= 4.0 && !user.hasWatched(movieId)) {
                    double similarity = calculateUserSimilarity(user, similarUser);
                    double weightedRating = rating * similarity;

                    movieScores.merge(movieId, weightedRating, Double::sum);
                    movieCounts.merge(movieId, 1, Integer::sum);
                }
            }
        }

        // Calculate average scores and sort
        List<Map.Entry<Integer, Double>> sortedScores = movieScores.entrySet().stream()
                .map(entry -> {
                    int movieId = entry.getKey();
                    double totalScore = entry.getValue();
                    int voteCount = movieCounts.get(movieId);
                    double averageScore = totalScore / voteCount;
                    return new AbstractMap.SimpleEntry<>(movieId, averageScore);
                })
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        // Convert to movies
        for (Map.Entry<Integer, Double> entry : sortedScores) {
            Movie movie = movieDatabase.getMovieById(entry.getKey());
            if (movie != null) {
                recommendations.add(movie);
                if (recommendations.size() >= count) break;
            }
        }

        return recommendations;
    }

    /**
     * Popular movie recommendations (fallback)
     */
    private List<Movie> getPopularMovieRecommendations(User user, int count) {
        return movieDatabase.getTopRatedMovies(count * 2).stream()
                .filter(movie -> !user.hasWatched(movie.getId()))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Find users with similar taste
     */
    private List<User> findSimilarUsers(User targetUser, int maxUsers) {
        List<User> allUsers = userDatabase.getAllUsers();
        Map<User, Double> similarities = new HashMap<>();

        for (User user : allUsers) {
            if (user.getUserId() != targetUser.getUserId()) {
                double similarity = calculateUserSimilarity(targetUser, user);
                if (similarity > 0.3) { // Minimum similarity threshold
                    similarities.put(user, similarity);
                }
            }
        }

        // Sort by similarity and return top users
        return similarities.entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(maxUsers)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Calculate similarity between two users using Pearson correlation
     */
    private double calculateUserSimilarity(User user1, User user2) {
        Map<Integer, Double> ratings1 = user1.getMovieRatings();
        Map<Integer, Double> ratings2 = user2.getMovieRatings();

        // Find common movies
        Set<Integer> commonMovies = new HashSet<>(ratings1.keySet());
        commonMovies.retainAll(ratings2.keySet());

        if (commonMovies.size() < 2) {
            return 0.0; // Need at least 2 common movies
        }

        // Calculate Pearson correlation
        double sum1 = 0, sum2 = 0, sum1Sq = 0, sum2Sq = 0, sumProducts = 0;

        for (Integer movieId : commonMovies) {
            double rating1 = ratings1.get(movieId);
            double rating2 = ratings2.get(movieId);

            sum1 += rating1;
            sum2 += rating2;
            sum1Sq += rating1 * rating1;
            sum2Sq += rating2 * rating2;
            sumProducts += rating1 * rating2;
        }

        double numerator = sumProducts - (sum1 * sum2 / commonMovies.size());
        double denominator = Math.sqrt((sum1Sq - sum1 * sum1 / commonMovies.size()) * 
                                      (sum2Sq - sum2 * sum2 / commonMovies.size()));

        if (denominator == 0) return 0.0;

        return Math.max(0, numerator / denominator);
    }

    /**
     * Get genres from user's highly rated movies
     */
    private List<String> getGenresFromUserRatings(User user) {
        Map<String, Double> genreScores = new HashMap<>();
        Map<String, Integer> genreCounts = new HashMap<>();

        for (Map.Entry<Integer, Double> entry : user.getMovieRatings().entrySet()) {
            Movie movie = movieDatabase.getMovieById(entry.getKey());
            if (movie != null && entry.getValue() >= 4.0) {
                String genre = movie.getGenre();
                genreScores.merge(genre, entry.getValue(), Double::sum);
                genreCounts.merge(genre, 1, Integer::sum);
            }
        }

        // Calculate average scores and sort
        return genreScores.entrySet().stream()
                .map(entry -> {
                    String genre = entry.getKey();
                    double totalScore = entry.getValue();
                    int count = genreCounts.get(genre);
                    return new AbstractMap.SimpleEntry<>(genre, totalScore / count);
                })
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Get similar movies based on genre and rating
     */
    public List<Movie> getSimilarMovies(Movie targetMovie, int count) {
        return movieDatabase.getAllMovies().stream()
                .filter(movie -> movie.getId() != targetMovie.getId())
                .filter(movie -> movie.getGenre().equals(targetMovie.getGenre()) || 
                               Math.abs(movie.getRating() - targetMovie.getRating()) <= 1.0)
                .sorted((m1, m2) -> {
                    double score1 = calculateMovieSimilarity(targetMovie, m1);
                    double score2 = calculateMovieSimilarity(targetMovie, m2);
                    return Double.compare(score2, score1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Calculate similarity between two movies
     */
    private double calculateMovieSimilarity(Movie movie1, Movie movie2) {
        double score = 0.0;

        // Genre similarity
        if (movie1.getGenre().equals(movie2.getGenre())) {
            score += 0.4;
        }

        // Rating similarity (closer ratings = higher similarity)
        double ratingDiff = Math.abs(movie1.getRating() - movie2.getRating());
        score += 0.3 * (1.0 - ratingDiff / 10.0);

        // Year similarity (same decade gets bonus)
        int yearDiff = Math.abs(movie1.getYear() - movie2.getYear());
        if (yearDiff <= 10) {
            score += 0.2 * (1.0 - yearDiff / 10.0);
        }

        // Director similarity
        if (movie1.getDirector().equals(movie2.getDirector())) {
            score += 0.1;
        }

        return score;
    }

    /**
     * Get trending movies (recently added high-rated movies)
     */
    public List<Movie> getTrendingMovies(int count) {
        return movieDatabase.getMoviesWithMinRating(7.5).stream()
                .filter(movie -> movie.getYear() >= 2000) // Recent movies
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
