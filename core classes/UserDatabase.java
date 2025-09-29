import java.util.*;
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

    private void initializeSampleUsers() {
        User alice = addUser("alice", "alice@email.com", "password123");
        alice.addFavoriteGenre("Action");
        alice.addFavoriteGenre("Sci-Fi");
        alice.rateMovie(1, 5.0);
        alice.rateMovie(6, 4.5);
        alice.rateMovie(7, 4.0);
        alice.rateMovie(14, 4.0);

        User bob = addUser("bob", "bob@email.com", "password123");
        bob.addFavoriteGenre("Drama");
        bob.addFavoriteGenre("Crime");
        bob.rateMovie(2, 5.0);
        bob.rateMovie(3, 4.5);
        bob.rateMovie(4, 4.0);
        bob.rateMovie(8, 4.5);

        User charlie = addUser("charlie", "charlie@email.com", "password123");
        charlie.addFavoriteGenre("Animation");
        charlie.addFavoriteGenre("Adventure");
        charlie.rateMovie(16, 5.0);
        charlie.rateMovie(17, 4.5);
        charlie.rateMovie(18, 4.0);
        charlie.rateMovie(15, 4.0);

        User diana = addUser("diana", "diana@email.com", "password123");
        diana.addFavoriteGenre("Romance");
        diana.addFavoriteGenre("Drama");
        diana.rateMovie(11, 5.0);
        diana.rateMovie(12, 4.0);
        diana.rateMovie(5, 4.5);
        diana.rateMovie(2, 4.0);
    }

    public User addUser(String username, String email, String password) {
        User user = new User(nextId++, username, email, password);
        users.add(user);
        userMap.put(user.getUserId(), user);
        usernameMap.put(username.toLowerCase(), user);
        return user;
    }

    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    public User getUserByUsername(String username) {
        return usernameMap.get(username.toLowerCase());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean usernameExists(String username) {
        return usernameMap.containsKey(username.toLowerCase());
    }

    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public int getUserCount() {
        return users.size();
    }

    public void printAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public List<User> getUsersByFavoriteGenre(String genre) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getFavoriteGenres().contains(genre)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> getMostActiveUsers(int limit) {
        return users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getMovieRatings().size(), u1.getMovieRatings().size()))
                .limit(limit)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
