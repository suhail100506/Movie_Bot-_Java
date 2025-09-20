import java.util.*;

/**
 * Simple User Database class to store and manage users
 */
public class UserDatabase {
    private List<User> users;
    private Map<Integer, User> userMap;
    private Map<String, User> usernameMap;
    private int nextId;

    public UserDatabase() {
        this.users = new ArrayList<>();
        this.userMap = new HashMap<>();
        this.usernameMap = new HashMap<>();
        this.nextId = 1;
        initializeSampleUsers();
    }

    // Initialize with sample users
    private void initializeSampleUsers() {
        // Create sample users with different preferences
        User alice = addUser("alice", "alice@email.com", "password123");
        alice.addFavoriteGenre("Action");
        alice.addFavoriteGenre("Sci-Fi");
        alice.rateMovie(1, 5.0); // The Dark Knight
        alice.rateMovie(6, 4.5); // Inception
        alice.rateMovie(7, 4.0); // The Matrix
        alice.rateMovie(14, 4.0); // The Avengers

        User bob = addUser("bob", "bob@email.com", "password123");
        bob.addFavoriteGenre("Drama");
        bob.addFavoriteGenre("Crime");
        bob.rateMovie(2, 5.0); // The Shawshank Redemption
        bob.rateMovie(3, 4.5); // The Godfather
        bob.rateMovie(4, 4.0); // Pulp Fiction
        bob.rateMovie(8, 4.5); // Goodfellas

        User charlie = addUser("charlie", "charlie@email.com", "password123");
        charlie.addFavoriteGenre("Animation");
        charlie.addFavoriteGenre("Adventure");
        charlie.rateMovie(16, 5.0); // The Lion King
        charlie.rateMovie(17, 4.5); // Finding Nemo
        charlie.rateMovie(18, 4.0); // Toy Story
        charlie.rateMovie(15, 4.0); // Jurassic Park

        User diana = addUser("diana", "diana@email.com", "password123");
        diana.addFavoriteGenre("Romance");
        diana.addFavoriteGenre("Drama");
        diana.rateMovie(11, 5.0); // Casablanca
        diana.rateMovie(12, 4.0); // Titanic
        diana.rateMovie(5, 4.5); // Forrest Gump
        diana.rateMovie(2, 4.0); // The Shawshank Redemption
    }

    // Add a new user
    public User addUser(String username, String email, String password) {
        User user = new User(nextId++, username, email, password);
        users.add(user);
        userMap.put(user.getUserId(), user);
        usernameMap.put(username.toLowerCase(), user);
        return user;
    }

    // Get user by ID
    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return usernameMap.get(username.toLowerCase());
    }

    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return usernameMap.containsKey(username.toLowerCase());
    }

    // Authenticate user
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Get user count
    public int getUserCount() {
        return users.size();
    }

    // Print all users
    public void printAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    // Get users by favorite genre
    public List<User> getUsersByFavoriteGenre(String genre) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getFavoriteGenres().contains(genre)) {
                result.add(user);
            }
        }
        return result;
    }

    // Get most active users (by number of ratings)
    public List<User> getMostActiveUsers(int limit) {
        return users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getMovieRatings().size(), u1.getMovieRatings().size()))
                .limit(limit)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
