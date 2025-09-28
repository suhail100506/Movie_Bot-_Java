import java.util.*;
/**
 * Represents an application user with profile fields, preferences,
 * watched movies, and per-movie ratings.
 *
 * <p>Instances of this class are simple data holders with small
 * convenience methods (e.g., add/remove favorites, rate movies).
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private List<String> favoriteGenres;
    private List<Integer> watchedMovieIds;
    private Map<Integer, Double> movieRatings;
    private Date joinDate;

    /**
     * Creates a new user with the provided identity and credentials.
     *
     * @param userId    unique identifier for the user
     * @param username  display/login name for the user
     * @param email     contact email
     * @param password  password (plain here; consider hashing in production)
     */
    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.favoriteGenres = new ArrayList<>();
        this.watchedMovieIds = new ArrayList<>();
        this.movieRatings = new HashMap<>();
        this.joinDate = new Date();
    }

    /**
     * Creates a user with empty lists/maps and a default join date.
     */
    public User() {
        this.favoriteGenres = new ArrayList<>();
        this.watchedMovieIds = new ArrayList<>();
        this.movieRatings = new HashMap<>();
        this.joinDate = new Date();
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getFavoriteGenres() { return favoriteGenres; }
    public void setFavoriteGenres(List<String> favoriteGenres) { this.favoriteGenres = favoriteGenres; }

    public List<Integer> getWatchedMovieIds() { return watchedMovieIds; }
    public void setWatchedMovieIds(List<Integer> watchedMovieIds) { this.watchedMovieIds = watchedMovieIds; }

    public Map<Integer, Double> getMovieRatings() { return movieRatings; }
    public void setMovieRatings(Map<Integer, Double> movieRatings) { this.movieRatings = movieRatings; }

    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    /** Adds a genre to the user's favorites if not already present. */
    public void addFavoriteGenre(String genre) {
        if (!favoriteGenres.contains(genre)) {
            favoriteGenres.add(genre);
        }
    }

    /** Removes a genre from the user's favorites (no-op if absent). */
    public void removeFavoriteGenre(String genre) {
        favoriteGenres.remove(genre);
    }

    /** Marks a movie as watched by this user. */
    public void addWatchedMovie(int movieId) {
        if (!watchedMovieIds.contains(movieId)) {
            watchedMovieIds.add(movieId);
        }
    }
    

    /**
     * Records a rating for a movie (1.0 to 5.0). Also marks the movie as watched.
     *
     * @param movieId id of the movie to rate
     * @param rating  rating value in the inclusive range [1.0, 5.0]
     */
    public void rateMovie(int movieId, double rating) {
        if (rating >= 1.0 && rating <= 5.0) {
            movieRatings.put(movieId, rating);
            addWatchedMovie(movieId);
        }
    }

    /**
     * Computes the arithmetic mean of all ratings given by the user.
     *
     * @return 0.0 if there are no ratings, otherwise the average rating
     */
    public double getAverageRating() {
        if (movieRatings.isEmpty()) return 0.0;

        double sum = 0.0;
        for (double rating : movieRatings.values()) {
            sum += rating;
        }
        return sum / movieRatings.size();
    }

    public boolean hasWatched(int movieId) {
        return watchedMovieIds.contains(movieId);
    }

    /** @return rating for the given movie id, or null if not rated. */
    public Double getRatingForMovie(int movieId) {
        return movieRatings.get(movieId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", favoriteGenres=" + favoriteGenres +
                ", watchedMovies=" + watchedMovieIds.size() +
                ", averageRating=" + String.format("%.1f", getAverageRating()) +
                '}';
    }
}
