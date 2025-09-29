
import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabase {

    private List<Movie> movies;
    private Map<Integer, Movie> movieMap;
    private int nextId;

    public MovieDatabase() {
        this.movies = new ArrayList<>();
        this.movieMap = new HashMap<>();
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
        addMovie("Inception", "Sci-Fi", 2010, 8.8, "Christopher Nolan",
                "A thief enters people's dreams to steal secrets", 148);
        addMovie("The Matrix", "Sci-Fi", 1999, 8.7, "The Wachowskis",
                "A computer programmer discovers reality is a simulation", 136);
        addMovie("Goodfellas", "Crime", 1990, 8.7, "Martin Scorsese",
                "The rise and fall of a mob associate over three decades", 146);
        addMovie("The Lord of the Rings: The Fellowship of the Ring", "Fantasy", 2001, 8.8, "Peter Jackson",
                "A hobbit embarks on a quest to destroy an ancient ring", 178);
        addMovie("Star Wars: Episode IV - A New Hope", "Sci-Fi", 1977, 8.6, "George Lucas",
                "Young Luke Skywalker joins the Rebel Alliance", 121);
        addMovie("Casablanca", "Romance", 1942, 8.5, "Michael Curtiz",
                "A cynical nightclub owner must choose between love and virtue", 102);
        addMovie("Titanic", "Romance", 1997, 7.8, "James Cameron",
                "A love story aboard the ill-fated RMS Titanic", 194);
        addMovie("Avatar", "Sci-Fi", 2009, 7.8, "James Cameron",
                "A marine explores an alien world and falls in love with its people", 162);
        addMovie("The Avengers", "Action", 2012, 8.0, "Joss Whedon",
                "Superheroes assemble to save Earth from an alien invasion", 143);
        addMovie("Jurassic Park", "Adventure", 1993, 8.1, "Steven Spielberg",
                "Scientists visit a theme park populated by cloned dinosaurs", 127);
        addMovie("The Lion King", "Animation", 1994, 8.5, "Roger Allers",
                "A young lion prince flees after his father's death", 88);
        addMovie("Finding Nemo", "Animation", 2003, 8.2, "Andrew Stanton",
                "A clownfish searches for his missing son across the ocean", 100);
        addMovie("Toy Story", "Animation", 1995, 8.3, "John Lasseter",
                "Toys come to life when humans aren't around", 81);
        addMovie("The Silence of the Lambs", "Thriller", 1991, 8.6, "Jonathan Demme",
                "An FBI trainee seeks help from the imprisoned Dr. Hannibal Lecter", 118);
        addMovie("Seven", "Thriller", 1995, 8.6, "David Fincher",
                "Two detectives hunt a serial killer who uses the seven deadly sins", 127);
    }

    public Movie addMovie(String title, String genre, int year, double rating,
            String director, String description, int duration) {
        Movie movie = new Movie(nextId++, title, genre, year, rating, director, description, duration);
        movies.add(movie);
        movieMap.put(movie.getId(), movie);
        return movie;
    }

    public Movie getMovieById(int id) {
        return movieMap.get(id);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public List<Movie> searchByTitle(String title) {
        return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByGenre(String genre) {
        return movies.stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByYear(int year) {
        return movies.stream()
                .filter(movie -> movie.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Movie> searchByDirector(String director) {
        return movies.stream()
                .filter(movie -> movie.getDirector().toLowerCase().contains(director.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesWithMinRating(double minRating) {
        return movies.stream()
                .filter(movie -> movie.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    public List<Movie> getTopRatedMovies(int limit) {
        return movies.stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenres(List<String> genres) {
        return movies.stream()
                .filter(movie -> genres.contains(movie.getGenre()))
                .collect(Collectors.toList());
    }

    public List<String> getAllGenres() {
        return movies.stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByYearRange(int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getYear() >= startYear && movie.getYear() <= endYear)
                .collect(Collectors.toList());
    }

    public List<Movie> getRandomMovies(int count) {
        List<Movie> shuffled = new ArrayList<>(movies);
        Collections.shuffle(shuffled);
        return shuffled.stream().limit(count).collect(Collectors.toList());
    }

    public int getMovieCount() {
        return movies.size();
    }

    public void printAllMovies() {
        System.out.println("\n=== ALL MOVIES ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    public void printMoviesByGenre(String genre) {
        List<Movie> genreMovies = searchByGenre(genre);
        System.out.println("\n=== " + genre.toUpperCase() + " MOVIES ===");
        for (Movie movie : genreMovies) {
            System.out.println(movie);
        }
    }
}

