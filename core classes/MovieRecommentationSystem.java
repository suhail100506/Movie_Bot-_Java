
import java.util.*;

/**
 * Main Movie Recommendation System Application
 */
public class MovieRecommendationSystem {

    private MovieDatabase movieDatabase;
    private UserDatabase userDatabase;
    private RecommendationEngine recommendationEngine;
    private Scanner scanner;
    private User currentUser;

    public MovieRecommendationSystem() {
        this.movieDatabase = new MovieDatabase();
        this.userDatabase = new UserDatabase();
        this.recommendationEngine = new RecommendationEngine(movieDatabase, userDatabase);
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    public static void main(String[] args) {
        MovieRecommendationSystem system = new MovieRecommendationSystem();
        system.run();
    }

    public void run() {
        System.out.println("🎬 Welcome to Movie Recommendation System! 🎬");
        System.out.println("==========================================");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== LOGIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Browse Movies (Guest)");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                browseMoviesAsGuest();
                break;
            case 4:
                System.out.println("Goodbye! 👋");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("Welcome, " + currentUser.getUsername() + "! 👋");
        System.out.println("1. Get Movie Recommendations");
        System.out.println("2. Browse All Movies");
        System.out.println("3. Search Movies");
        System.out.println("4. Rate a Movie");
        System.out.println("5. View My Ratings");
        System.out.println("6. Manage Profile");
        System.out.println("7. View Similar Movies");
        System.out.println("8. View Trending Movies");
        System.out.println("9. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                getRecommendations();
                break;
            case 2:
                browseAllMovies();
                break;
            case 3:
                searchMovies();
                break;
            case 4:
                rateMovie();
                break;
            case 5:
                viewMyRatings();
                break;
            case 6:
                manageProfile();
                break;
            case 7:
                viewSimilarMovies();
                break;
            case 8:
                viewTrendingMovies();
                break;
            case 9:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = userDatabase.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getUsername() + "! 🎉");
        } else {
            System.out.println("Invalid credentials. Please try again. ❌");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (userDatabase.usernameExists(username)) {
            System.out.println("Username already exists. Please choose another. ❌");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User newUser = userDatabase.addUser(username, email, password);
        System.out.println("Registration successful! Welcome, " + username + "! 🎉");

        // Set favorite genres
        setFavoriteGenres(newUser);

        currentUser = newUser;
    }

    private void setFavoriteGenres(User user) {
        System.out.println("\nLet's set your favorite movie genres:");
        List<String> allGenres = movieDatabase.getAllGenres();

        for (int i = 0; i < allGenres.size(); i++) {
            System.out.println((i + 1) + ". " + allGenres.get(i));
        }

        System.out.println("Enter genre numbers separated by commas (e.g., 1,3,5): ");
        String input = scanner.nextLine().trim();

        if (!input.isEmpty()) {
            String[] numbers = input.split(",");
            for (String numStr : numbers) {
                try {
                    int index = Integer.parseInt(numStr.trim()) - 1;
                    if (index >= 0 && index < allGenres.size()) {
                        user.addFavoriteGenre(allGenres.get(index));
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            }
        }

        System.out.println("Favorite genres set: " + user.getFavoriteGenres());
    }

    private void getRecommendations() {
        System.out.println("\n🎯 Getting personalized recommendations for you...");

        List<Movie> recommendations = recommendationEngine.getRecommendations(currentUser, 10);

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available right now. Try rating some movies first!");
        } else {
            System.out.println("\n🎬 Here are your personalized movie recommendations:");
            for (int i = 0; i < recommendations.size(); i++) {
                System.out.println((i + 1) + ". " + recommendations.get(i));
            }
        }
    }

    private void browseAllMovies() {
        System.out.println("\n=== ALL MOVIES ===");
        List<Movie> movies = movieDatabase.getAllMovies();
        displayMovieList(movies);
    }

    private void browseMoviesAsGuest() {
        System.out.println("\n=== BROWSING AS GUEST ===");
        System.out.println("1. View All Movies");
        System.out.println("2. View Top Rated Movies");
        System.out.println("3. View Movies by Genre");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                browseAllMovies();
                break;
            case 2:
                List<Movie> topRated = movieDatabase.getTopRatedMovies(10);
                System.out.println("\n🏆 TOP RATED MOVIES:");
                displayMovieList(topRated);
                break;
            case 3:
                browseByGenreGuest();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void browseByGenreGuest() {
        System.out.println("\nAvailable genres:");
        List<String> genres = movieDatabase.getAllGenres();
        for (int i = 0; i < genres.size(); i++) {
            System.out.println((i + 1) + ". " + genres.get(i));
        }

        System.out.print("Choose a genre (number): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= genres.size()) {
            String selectedGenre = genres.get(choice - 1);
            List<Movie> genreMovies = movieDatabase.searchByGenre(selectedGenre);
            System.out.println("\n" + selectedGenre.toUpperCase() + " MOVIES:");
            displayMovieList(genreMovies);
        }
    }

    private void searchMovies() {
        System.out.println("\n=== SEARCH MOVIES ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Genre");
        System.out.println("3. Search by Director");
        System.out.println("4. Search by Year");
        System.out.print("Choose search type: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                searchByTitle();
                break;
            case 2:
                searchByGenre();
                break;
            case 3:
                searchByDirector();
                break;
            case 4:
                searchByYear();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void searchByTitle() {
        System.out.print("Enter movie title (or part of it): ");
        String title = scanner.nextLine().trim();

        List<Movie> results = movieDatabase.searchByTitle(title);
        if (results.isEmpty()) {
            System.out.println("No movies found with that title. 😞");
        } else {
            System.out.println("\n🔍 Search results:");
            displayMovieList(results);
        }
    }

    private void searchByGenre() {
        System.out.println("\nAvailable genres:");
        List<String> genres = movieDatabase.getAllGenres();
        for (int i = 0; i < genres.size(); i++) {
            System.out.println((i + 1) + ". " + genres.get(i));
        }

        System.out.print("Choose a genre (number): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= genres.size()) {
            String selectedGenre = genres.get(choice - 1);
            List<Movie> results = movieDatabase.searchByGenre(selectedGenre);
            System.out.println("\n" + selectedGenre.toUpperCase() + " MOVIES:");
            displayMovieList(results);
        }
    }

    private void searchByDirector() {
        System.out.print("Enter director name (or part of it): ");
        String director = scanner.nextLine().trim();

        List<Movie> results = movieDatabase.searchByDirector(director);
        if (results.isEmpty()) {
            System.out.println("No movies found by that director. 😞");
        } else {
            System.out.println("\n🎬 Movies by directors matching '" + director + "':");
            displayMovieList(results);
        }
    }

    private void searchByYear() {
        System.out.print("Enter year: ");
        int year = getIntInput();

        List<Movie> results = movieDatabase.searchByYear(year);
        if (results.isEmpty()) {
            System.out.println("No movies found from " + year + ". 😞");
        } else {
            System.out.println("\n📅 Movies from " + year + ":");
            displayMovieList(results);
        }
    }

    private void rateMovie() {
        System.out.print("Enter movie ID to rate: ");
        int movieId = getIntInput();

        Movie movie = movieDatabase.getMovieById(movieId);
        if (movie == null) {
            System.out.println("Movie not found. ❌");
            return;
        }

        System.out.println("Movie: " + movie.getTitle());
        System.out.print("Enter your rating (1.0 - 5.0): ");
        double rating = getDoubleInput();

        if (rating >= 1.0 && rating <= 5.0) {
            currentUser.rateMovie(movieId, rating);
            System.out.println("Rating saved! ⭐ You rated '" + movie.getTitle() + "' " + rating + "/5.0");
        } else {
            System.out.println("Invalid rating. Please enter a value between 1.0 and 5.0. ❌");
        }
    }

    private void viewMyRatings() {
        Map<Integer, Double> ratings = currentUser.getMovieRatings();

        if (ratings.isEmpty()) {
            System.out.println("You haven't rated any movies yet. 📝");
            return;
        }

        System.out.println("\n⭐ YOUR MOVIE RATINGS:");
        for (Map.Entry<Integer, Double> entry : ratings.entrySet()) {
            Movie movie = movieDatabase.getMovieById(entry.getKey());
            if (movie != null) {
                System.out.printf("%s - %.1f/5.0 ⭐%n", movie.getTitle(), entry.getValue());
            }
        }

        System.out.printf("\nAverage rating: %.1f/5.0%n", currentUser.getAverageRating());
        System.out.println("Total rated movies: " + ratings.size());
    }

    private void manageProfile() {
        System.out.println("\n=== PROFILE MANAGEMENT ===");
        System.out.println("Current user: " + currentUser.getUsername());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Favorite genres: " + currentUser.getFavoriteGenres());
        System.out.println("Movies watched: " + currentUser.getWatchedMovieIds().size());
        System.out.println("Average rating: " + String.format("%.1f", currentUser.getAverageRating()));

        System.out.println("\n1. Update Favorite Genres");
        System.out.println("2. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        if (choice == 1) {
            updateFavoriteGenres();
        }
    }

    private void updateFavoriteGenres() {
        System.out.println("\nCurrent favorite genres: " + currentUser.getFavoriteGenres());
        System.out.println("\n1. Add a genre");
        System.out.println("2. Remove a genre");
        System.out.println("3. Replace all genres");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                addFavoriteGenre();
                break;
            case 2:
                removeFavoriteGenre();
                break;
            case 3:
                currentUser.getFavoriteGenres().clear();
                setFavoriteGenres(currentUser);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addFavoriteGenre() {
        List<String> allGenres = movieDatabase.getAllGenres();
        System.out.println("\nAvailable genres:");
        for (int i = 0; i < allGenres.size(); i++) {
            System.out.println((i + 1) + ". " + allGenres.get(i));
        }

        System.out.print("Choose a genre to add (number): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= allGenres.size()) {
            String genre = allGenres.get(choice - 1);
            currentUser.addFavoriteGenre(genre);
            System.out.println("Added '" + genre + "' to your favorite genres! ✅");
        }
    }

    private void removeFavoriteGenre() {
        List<String> favoriteGenres = currentUser.getFavoriteGenres();
        if (favoriteGenres.isEmpty()) {
            System.out.println("You have no favorite genres to remove.");
            return;
        }

        System.out.println("\nYour favorite genres:");
        for (int i = 0; i < favoriteGenres.size(); i++) {
            System.out.println((i + 1) + ". " + favoriteGenres.get(i));
        }

        System.out.print("Choose a genre to remove (number): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= favoriteGenres.size()) {
            String genre = favoriteGenres.get(choice - 1);
            currentUser.removeFavoriteGenre(genre);
            System.out.println("Removed '" + genre + "' from your favorite genres! ✅");
        }
    }

    private void viewSimilarMovies() {
        System.out.print("Enter movie ID to find similar movies: ");
        int movieId = getIntInput();

        Movie movie = movieDatabase.getMovieById(movieId);
        if (movie == null) {
            System.out.println("Movie not found. ❌");
            return;
        }

        List<Movie> similarMovies = recommendationEngine.getSimilarMovies(movie, 5);

        System.out.println("\n🎭 Movies similar to '" + movie.getTitle() + "':");
        if (similarMovies.isEmpty()) {
            System.out.println("No similar movies found.");
        } else {
            displayMovieList(similarMovies);
        }
    }

    private void viewTrendingMovies() {
        List<Movie> trendingMovies = recommendationEngine.getTrendingMovies(10);

        System.out.println("\n🔥 TRENDING MOVIES:");
        if (trendingMovies.isEmpty()) {
            System.out.println("No trending movies available.");
        } else {
            displayMovieList(trendingMovies);
        }
    }

    private void displayMovieList(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("No movies to display.");
            return;
        }

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("%d. [ID:%d] %s (%d) - %s - %.1f⭐ - %s%n",
                    i + 1, movie.getId(), movie.getTitle(), movie.getYear(),
                    movie.getGenre(), movie.getRating(), movie.getFormattedDuration());
        }
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
