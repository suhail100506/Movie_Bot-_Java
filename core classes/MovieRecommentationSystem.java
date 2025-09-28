
import java.util.*;

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
                break;
            default:
        }
    }

    private void login() {
        // Removed print statement
        String username = scanner.nextLine().trim();
        // Removed print statement
        String password = scanner.nextLine().trim();

        User user = userDatabase.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            // Removed print statement
        } else {
            // Removed print statement
        }
    }

    private void register() {
        // Removed print statement
        String username = scanner.nextLine().trim();

        if (userDatabase.usernameExists(username)) {
            // Removed print statement
            return;
        }

        // Removed print statement
        String email = scanner.nextLine().trim();
        // Removed print statement
        String password = scanner.nextLine().trim();

        User newUser = userDatabase.addUser(username, email, password);
        // Removed print statement

        // Removed comment
        setFavoriteGenres(newUser);

        currentUser = newUser;
    }

    private void setFavoriteGenres(User user) {
        // Removed print statement
        List<String> allGenres = movieDatabase.getAllGenres();

        for (int i = 0; i < allGenres.size(); i++) {
            // Removed print statement
        }

        // Removed print statement
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

        // Removed print statement
    }

    private void getRecommendations() {
        // Removed print statement

        List<Movie> recommendations = recommendationEngine.getRecommendations(currentUser, 10);

        if (recommendations.isEmpty()) {
            // Removed print statement
        } else {
            // Removed print statement
            for (int i = 0; i < recommendations.size(); i++) {
                // Removed print statement
            }
        }
    }

    private void browseAllMovies() {
        // Removed print statement
        List<Movie> movies = movieDatabase.getAllMovies();
        displayMovieList(movies);
    }

    private void browseMoviesAsGuest() {
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement

        int choice = getIntInput();

        switch (choice) {
            case 1:
                browseAllMovies();
                break;
            case 2:
                List<Movie> topRated = movieDatabase.getTopRatedMovies(10);
                // Removed print statement
                displayMovieList(topRated);
                break;
            case 3:
                browseByGenreGuest();
                break;
            case 4:
                return;
            default:
            // Removed print statement
        }
    }

    private void browseByGenreGuest() {
        // Removed print statement
        List<String> genres = movieDatabase.getAllGenres();
        for (int i = 0; i < genres.size(); i++) {
            // Removed print statement
        }

        // Removed print statement
        int choice = getIntInput();

        if (choice > 0 && choice <= genres.size()) {
            String selectedGenre = genres.get(choice - 1);
            List<Movie> genreMovies = movieDatabase.searchByGenre(selectedGenre);
            // Removed print statement
            displayMovieList(genreMovies);
        }
    }

    private void searchMovies() {
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement

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
            // Removed print statement
        }
    }

    private void searchByTitle() {
        // Removed print statement
        String title = scanner.nextLine().trim();

        List<Movie> results = movieDatabase.searchByTitle(title);
        if (results.isEmpty()) {
            // Removed print statement
        } else {
            // Removed print statement
            displayMovieList(results);
        }
    }

    private void searchByGenre() {
        // Removed print statement
        List<String> genres = movieDatabase.getAllGenres();
        for (int i = 0; i < genres.size(); i++) {
            // Removed print statement
        }

        // Removed print statement
        int choice = getIntInput();

        if (choice > 0 && choice <= genres.size()) {
            String selectedGenre = genres.get(choice - 1);
            List<Movie> results = movieDatabase.searchByGenre(selectedGenre);
            // Removed print statement
            displayMovieList(results);
        }
    }

    private void searchByDirector() {
        // Removed print statement
        String director = scanner.nextLine().trim();

        List<Movie> results = movieDatabase.searchByDirector(director);
        if (results.isEmpty()) {
            // Removed print statement
        } else {
            // Removed print statement
            displayMovieList(results);
        }
    }

    private void searchByYear() {
        // Removed print statement
        int year = getIntInput();

        List<Movie> results = movieDatabase.searchByYear(year);
        if (results.isEmpty()) {
            // Removed print statement
        } else {
            // Removed print statement
            displayMovieList(results);
        }
    }

    private void rateMovie() {
        // Removed print statement
        int movieId = getIntInput();

        Movie movie = movieDatabase.getMovieById(movieId);
        if (movie == null) {
            // Removed print statement
            return;
        }

        // Removed print statement
        // Removed print statement
        double rating = getDoubleInput();

        if (rating >= 1.0 && rating <= 5.0) {
            currentUser.rateMovie(movieId, rating);
            // Removed print statement
        } else {
            // Removed print statement
        }
    }

    private void viewMyRatings() {
        Map<Integer, Double> ratings = currentUser.getMovieRatings();

        if (ratings.isEmpty()) {
            // Removed print statement
            return;
        }

        // Removed print statement
        for (Map.Entry<Integer, Double> entry : ratings.entrySet()) {
            Movie movie = movieDatabase.getMovieById(entry.getKey());
            if (movie != null) {
                // Removed print statement
            }
        }

        // Removed print statement
        // Removed print statement
    }

    private void manageProfile() {
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement

        // Removed print statement
        // Removed print statement
        // Removed print statement
        int choice = getIntInput();
        if (choice == 1) {
            updateFavoriteGenres();
        }
    }

    private void updateFavoriteGenres() {
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement
        // Removed print statement

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
            // Removed print statement
        }
    }

    private void addFavoriteGenre() {
        List<String> allGenres = movieDatabase.getAllGenres();
        // Removed print statement
        for (int i = 0; i < allGenres.size(); i++) {
            // Removed print statement
        }

        // Removed print statement
        int choice = getIntInput();

        if (choice > 0 && choice <= allGenres.size()) {
            String genre = allGenres.get(choice - 1);
            currentUser.addFavoriteGenre(genre);
            // Removed print statement
        }
    }

    private void removeFavoriteGenre() {
        List<String> favoriteGenres = currentUser.getFavoriteGenres();
        if (favoriteGenres.isEmpty()) {
            return;
        }
    }

    private void viewSimilarMovies() {
        // Removed print statement
        int movieId = getIntInput();

        Movie movie = movieDatabase.getMovieById(movieId);
        if (movie == null) {
            // Removed print statement
            return;
        }

        List<Movie> similarMovies = recommendationEngine.getSimilarMovies(movie, 5);

        // Removed print statement
        if (similarMovies.isEmpty()) {
            // Removed print statement
        } else {
            displayMovieList(similarMovies);
        }
    }

    private void viewTrendingMovies() {
        List<Movie> trendingMovies = recommendationEngine.getTrendingMovies(10);
        if (trendingMovies.isEmpty()) {
        } else {
            displayMovieList(trendingMovies);
        }
    }

    private void displayMovieList(List<Movie> movies) {
        if (movies.isEmpty()) {
            return;
        }
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
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
