import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabase {

    private List<Movie> movies;
    private Map<Integer, Movie> movieMap;
    private int nextId;

    public MovieDatabase() {
        this.movies = new ArrayList<>();
        this.movieMap = new HashMap ();
        this.nextId = 1;
        initializeSampleMovies();
    }

    private void initializeSampleMovies() {
        addMovie("The Dark Knight", "Action", 2008, 9.0, "Christopher Nolan",
                "Batman faces the Joker in this thrilling superhero film", 152);
        addMovie("The Shawshank Redemption", "Drama", 1994, 9.3, "Frank Darabont",
                "Two imprisoned men bond over years, finding solace and redemption", 142);
        addMovie("The Godfather", "Crime", 1972, 9.2, "Francis Ford Coppola",
                "The aging patriarch transfers control of his crime empire to his son", 175);
        addMovie("Pulp Fiction", "Crime", 1994, 8.9, "Quentin Tarantino",
                "Interconnected stories of crime and redemption in Los Angeles", 154);
        addMovie("Forrest Gump", "Drama", 1994, 8.8, "Robert Zemeckis",
                "The life journey of a simple man who achieves extraordinary things", 142);
    }

    public void addMovie(String title, String genre, int year, double rating, String director, String description, int duration) {
        Movie movie = new Movie(nextId, title, genre, year, rating, director, description, duration);
        movies.add(movie);
        movieMap.put(nextId, movie);
        nextId++;
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie getMovieById(int id) {
        return movieMap.get(id);
    }

    public List<Movie> searchByTitle(String title) {
        return movies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByGenre(String genre) {
        return movies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByDirector(String director) {
        return movies.stream()
                .filter(m -> m.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByYear(int year) {
        return movies.stream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesWithMinRating(double minRating) {
        return movies.stream()
                .filter(m -> m.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    public List<Movie> getTopRatedMovies(int count) {
        return movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
