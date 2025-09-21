
import static spark.Spark.*;

public class WebServer {

    public static void main(String[] args) {
        port(8080); // Changed to avoid port conflict
        // Serve static files from the project root
        staticFiles.externalLocation("d:/project new/movie bot java");
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });
    }
}
// Commit 1: Update homepage layout
// Commit 2: Add genre filter
// Commit 3: Improve error handling
// Commit 4: Add logout functionality
// Commit 5: Enhance movie details page
// Commit 2: Fix login bug
// Commit 3: Add movie search feature
// Commit 4: Improve CSS styling
// Commit 5: Refactor recommendation logic
