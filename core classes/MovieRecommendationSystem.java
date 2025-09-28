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
        System.out.println("Welcome to Movie Recommendation System!");
        System.out.println("==========================================");
    }
}
