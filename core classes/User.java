import java.util.*;

/**
 * Simple User class representing a user in the system
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private List<String> favoriteGenres;
    private List<Integer> watchedMovieIds;
    private Map<Integer, Double> movieRatings; // movieId -> rating
    private Date joinDate;

    // Constructor
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

    // Default constructor
    public User() {
        this.favoriteGenres = new ArrayList<>();
        this.watchedMovieIds = new ArrayList<>();
        this.movieRatings = new HashMap<>();
        this.joinDate = new Date();
    }

    // Getters and Setters
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

    // Utility methods
    public void addFavoriteGenre(String genre) {
        if (!favoriteGenres.contains(genre)) {
            favoriteGenres.add(genre);
        }
    }

    public void removeFavoriteGenre(String genre) {
        favoriteGenres.remove(genre);
    }

    public void addWatchedMovie(int movieId) {
        if (!watchedMovieIds.contains(movieId)) {
            watchedMovieIds.add(movieId);
        }
    }

    public void rateMovie(int movieId, double rating) {
        if (rating >= 1.0 && rating <= 5.0) {
            movieRatings.put(movieId, rating);
            addWatchedMovie(movieId);
        }
    }

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
